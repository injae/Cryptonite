package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Server_Login extends Server_Funtion
{
	Server_DataBase db = Server_DataBase.getInstance();
	
	@Override
	public void Checker(byte[] packet, Server_Client_Activity activity) 
	{
		_activity = activity;
		_activity.receive.setAllocate(500).setAllocate(500);
		_packetMaxCount=packet[1];	
	}

	@Override
	public void running()
	{
		String id = new String( _activity.receive.getByte()).trim();
		String password= new String( _activity.receive.getByte()).trim();
		
		try
	    {
		   byte[] _checkLogin=new byte[1024];
	       ResultSet _rs  = db.Query("select * from test where id like '"+ id +"';");
	       	    
	       if(!_rs.next()) 
	       {	
	    	   _checkLogin[0]=1; 
	       }
	       else
	       { 
		       String _get_pwd = _rs.getString(3);
		       int _get_count = _rs.getInt(5);
		       
		       if(_get_pwd.equals(password)) 
		       { 
		    	   _checkLogin[0]=2;
		    	   if(_get_count==1)
		    	   {
		    		   _checkLogin[1]=1;
		    		   db.Update("update test set count=2 where id='"+id+"';");
		    	   }
		    	}
		       else 					 { _checkLogin[0]=3; }   
	       }
	       _activity.send.setPacket(_checkLogin).write();
	    }
	    catch(SQLException e1)
	    {
	       e1.printStackTrace();
	    }         

	}
}
