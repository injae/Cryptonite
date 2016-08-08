package Server;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;

import Function.PacketRule;

public class Server_SignUp extends Server_Funtion  implements PacketRule
{
	Server_DataBase db=Server_DataBase.getInstance();
	byte _mode = 0;
	
	@Override
	public void Checker(byte[] packet) 
	{
		_mode=packet[1];
		_packetMaxCount = packet[2];	
	}

	@Override
	public void running(Server_Client_Activity activity) 
	{		
		switch(_mode)
		{
		case  DUPLICATION_CHECK_FUNCTION:
			duplicationCheck(activity); break;
		case SIGN_UP_FUNCTION:
			sign_up(activity); 			break;
		}
	}
	
	private void duplicationCheck(Server_Client_Activity activity)
	{
		try
	    {
		   String client_id = new String( activity._receiveQueue.remove()).trim();
		    
		   byte[] _checkid=new byte[2];
	       ResultSet _rs  = db.Query("select * from test where id like '"+ client_id +"';");
	
	       if(!_rs.next()) { _checkid[0]=1; }
	       else 		   { _checkid[0]=2; }
	       
	       activity.Sender(_checkid);
	       activity.send();
	    }
	    catch(SQLException e1)
	    {
	       e1.printStackTrace();
	    }   
	}
	
	private void sign_up(Server_Client_Activity activity)
	{
		int count=1;
		System.out.println("SignUp running");
		
		Charset cs =Charset.forName("UTF-8");
		ByteBuffer bb=ByteBuffer.allocateDirect(1024);			
		bb.put(activity._receiveQueue.remove()); bb.flip();			
		String name=cs.decode(bb).toString().trim();
		
		String id=new String(activity._receiveQueue.remove()).trim();
		String password=new String(activity._receiveQueue.remove()).trim();
		String email=new String(activity._receiveQueue.remove()).trim();

		db.Update("INSERT INTO TEST VALUES("+"'"+name+"','"+id+"','"+password+"','"+email+"','"+count+"');");
	}
	
}
