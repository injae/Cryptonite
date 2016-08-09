package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Server_Login extends Server_Funtion
{
	Server_DataBase db = Server_DataBase.getInstance();
	
	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount=packet[1];	
	}

	@Override
	public void running(Server_Client_Activity activity)
	{
		String id = new String( activity._receiveQueue.remove()).trim();
		String password= new String( activity._receiveQueue.remove()).trim();
		
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
	       activity.Sender(_checkLogin);
	       activity.send();
	    }
	    catch(SQLException e1)
	    {
	       e1.printStackTrace();
	    }         

	}
}
