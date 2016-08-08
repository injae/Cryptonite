package Server;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server_SignUp extends Server_Funtion 
{
	byte _mode=0;
	@Override
	public void Checker(byte[] packet) 
	{
		_mode=packet[1];
		_packetMaxCount = packet[2];	
	}

	@Override
	public void running(Server_Client_Activity activity) 
	{		Server_DataBase db;
			db=Server_DataBase.getInstance();
		if(_mode==1)
		{
			
			String client_id = new String( activity._receiveQueue.remove()).trim();
		    
			byte[] _checkid=new byte[2];
			try
		    {
		       ResultSet _rs  = db.Query("select * from test where id like '"+ client_id +"';");
		
		       
		       if(!_rs.next()) 
		       {
		    	   _checkid[0]=1; 
		       }
		       else if (_rs.wasNull()){
		    	   _checkid[0]=2;
		       }
		       else
		       { 
			      _checkid[0]=3;
		       }
		       System.out.println(_checkid[0]);
		       activity.Sender(_checkid);
		       activity.send();
		    }
		    catch(SQLException e1)
		    {
		       e1.printStackTrace();
		    }         
		}
			
		else if(_mode==2)
		{
			int count=1;
			System.out.println("SignUp running");
			
			Charset cs =Charset.forName("UTF-8");
			ByteBuffer bb=ByteBuffer.allocateDirect(1024);
			
			bb.put(activity._receiveQueue.remove());
			bb.flip();
			String name=cs.decode(bb).toString().trim();
			
			String id=new String(activity._receiveQueue.remove()).trim();
			String password=new String(activity._receiveQueue.remove()).trim();
			String email=new String(activity._receiveQueue.remove()).trim();
			
			System.out.println(name);
			System.out.println(id);
			System.out.println(password);
			System.out.println(email);
			
	
			System.out.println(activity._receiveQueue.isEmpty());
			
			db.Update("INSERT INTO TEST VALUES("+"'"+name+"','"+id+"','"+password+"','"+email+"','"+count+"');");
		}
		
	}
}
