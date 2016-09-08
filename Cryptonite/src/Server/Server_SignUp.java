package Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;

import Crypto.aesKeyGenerator;
import Function.PacketRule;

public class Server_SignUp extends Server_Funtion  implements PacketRule
{
	public Server_SignUp(Server_Client_Activity activity) {
		super(activity);
		// TODO 磊悼 积己等 积己磊 胶庞
	}

	Server_DataBase db=Server_DataBase.getInstance();
	byte _mode = 0;
	
	@Override
	public void Checker(byte[] packet) 
	{
		_mode=packet[1];
		if(_mode == DUPLICATION_CHECK_FUNCTION) { _activity.receive.setAllocate(500); }
		else if(_mode == SIGN_UP_FUNCTION) 		{ _activity.receive.setAllocate(500).setAllocate(500).setAllocate(500).setAllocate(500);}
		_packetMaxCount = packet[2];	
	}

	@Override
	public void running(int count) throws IOException 
	{		
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			switch(_mode)
			{
			case  DUPLICATION_CHECK_FUNCTION:
				duplicationCheck(); break;
			case SIGN_UP_FUNCTION:
				sign_up(); 			break;
			}
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
	       
	       _activity.send.setPacket(_checkid,2).write();
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
		
		aesKeyGenerator ukg = new aesKeyGenerator();
		ukg.init();
		String aeskey= ukg.getAesKeyToString();
		String salt= ukg.getSaltToString();
		String iteration= ukg.getIterationCountToString();
		
		String usCode = Server_Code_Manager.getInstance().getUsCode();
		int code = Integer.parseInt(usCode.substring(1));
		String gpCode = "NULL";
		result = db.Update("INSERT INTO TEST VALUES('"+name+"','"+id+"','"+password+"','"+email+"',"+count+","+code+",'"+aeskey+"','"+salt+"','"+iteration  +"','"+gpCode+"');");

		//send result to client
		byte[] resultPacket = new byte[1];
		if(result)
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
