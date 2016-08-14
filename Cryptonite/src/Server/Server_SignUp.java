package Server;

import java.io.IOException;
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
	public void Checker(byte[] packet, Server_Client_Activity activity) 
	{
		_activity = activity;
		_mode=packet[1];
		if(_mode == DUPLICATION_CHECK_FUNCTION){ _activity.receive.setAllocate(500); }
		else if(_mode == SIGN_UP_FUNCTION) { _activity.receive.setAllocate(500).setAllocate(500).setAllocate(500).setAllocate(500).setAllocate(500).setAllocate(500).setAllocate(500);}
		_packetMaxCount = packet[2];	
	}

	@Override
	public void running() throws IOException 
	{		
		switch(_mode)
		{
		case  DUPLICATION_CHECK_FUNCTION:
			duplicationCheck(); break;
		case SIGN_UP_FUNCTION:
			sign_up(); 			break;
		}
	}
	
	private void duplicationCheck() throws IOException
	{
		try
	    {
		   String client_id = new String( _activity.receive.getByte()).trim();
		    
		   byte[] _checkid=new byte[2];
	       ResultSet _rs  = db.Query("select * from test where id like '"+ client_id +"';");
	
	       if(!_rs.next()) { _checkid[0]=1; }
	       else 		   { _checkid[0]=2; }
	       
	       _activity.send.setPacket(_checkid).write();
	    }
	    catch(SQLException e1)
	    {
	       e1.printStackTrace();
	    }   
	}
	
	private void sign_up() throws IOException
	{
		int count=1;
		boolean result;
		System.out.println("SignUp running");
		
		Charset cs =Charset.forName("UTF-8");
		ByteBuffer bb=ByteBuffer.allocateDirect(1024);			
		bb.put(_activity.receive.getByte()); bb.flip();			
		
		String name=cs.decode(bb).toString().trim();
		String id=new String(_activity.receive.getByte()).trim();
		String password=new String(_activity.receive.getByte()).trim();
		String email=new String(_activity.receive.getByte()).trim();
		String aeskey=new String(_activity.receive.getByte()).trim();
		String salt=new String(_activity.receive.getByte()).trim();
		String iteration=new String(_activity.receive.getByte()).trim();
		
		String usCode = Server_UserCode_Manager.getInstance().getUsCode();
		
		/*byte[] AES_key=_activity.receive.getByte();
		byte[] _salt=_activity.receive.getByte();
		byte[] _iteration=_activity.receive.getByte();*/

		/*String aeskey=new String(AES_key).trim();
		String salt=new String(_salt).trim();
		String iteration=new String(_iteration).trim();*/
		/*System.out.println(name);
		System.out.println(id);
		System.out.println(aeskey);
		System.out.println(salt);
		System.out.println(iteration);
*/		System.out.println("uscode"+usCode);
		result = db.Update("INSERT INTO TEST VALUES("+"'"+name+"','"+id+"','"+password+"','"+email+"','"+count+"','"+usCode+"','"+aeskey+"','"+salt+"','"+iteration+"');");

		//send result to client
		byte[] resultPacket = new byte[1];
		if(result == true)
		{
			resultPacket[0] = 1;
			_activity.send.setPacket(resultPacket).write();
		}
		else
		{
			resultPacket[0] = 0;
			_activity.send.setPacket(resultPacket).write();
		}
		
		
	}
	
}
