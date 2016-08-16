package Server;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.omg.CORBA.ACTIVITY_COMPLETED;

public class Server_Login extends Server_Funtion
{
	public Server_Login(Server_Client_Activity activity) {
		super(activity);
		// TODO 磊悼 积己等 积己磊 胶庞
	}

	Server_DataBase db = Server_DataBase.getInstance();
	
	@Override
	public void Checker(byte[] packet) 
	{
		_activity.receive.setAllocate(500).setAllocate(500);
		_packetMaxCount=packet[1];	
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			String id = new String( _activity.receive.getByte()).trim();
			String password= new String( _activity.receive.getByte()).trim();
			
			try
		    {
			   byte[] _checkLogin=new byte[1024];
		       ResultSet rs  = db.Query("select * from test where id like '"+ id +"';");   
		       if(!rs.next()) 
		       {	 
		    	   _checkLogin[0]=1; 
		       }
		       else
		       { 	
			       String get_pwd = rs.getString(3);
			       int get_count = rs.getInt(5);
			       if(get_pwd.equals(password)) 
			       { 
				       String usCode = "@" + rs.getInt(6);
		    		   Server_Client_Manager.getInstance().login(_activity.getClientCode(), usCode);
		    		   _activity.setClientCode(usCode);
			    	   _checkLogin[0]=2;
			    	   if(get_count==1)
			    	   {
			    		   _checkLogin[1]=1;
			    		   db.Update("update test set count=2 where id='"+id+"';");
			    	   }
	
			    	}
			       else 	{ _checkLogin[0]=3; }   
		       }
		       _activity.send.setPacket(_checkLogin).write();
		    }
		    catch(SQLException e1)
		    {
		       e1.printStackTrace();
		    }         
		}
	}
}
