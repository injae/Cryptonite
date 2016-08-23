package Server;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.omg.CORBA.ACTIVITY_COMPLETED;

public class Server_Login extends Server_Funtion
{
	public Server_Login(Server_Client_Activity activity) {
		super(activity);
		// TODO 磊悼 积己等 积己磊 胶庞
	}

	Server_DataBase db = Server_DataBase.getInstance();
	 ArrayList<String> gpcode = new ArrayList<String>();
     ArrayList<String> gpname = new ArrayList<String>();
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
			int get_count=0;
			
			byte[] groupcount=new byte[1];
			
			String myname = null;
			String mygrouplist = null;
			String aeskey = null;
			String uscode = null;
			String id = new String( _activity.receive.getByte()).trim();
			String password= new String( _activity.receive.getByte()).trim();
			try
		    {
			   byte[] _checkLogin = new byte[1024];
		       ResultSet rs  = db.Query("select * from test where id like '"+ id +"';");  
		       if(!rs.next()) 
		       {	 
		    	   _checkLogin[0]=1; 
		       }
		       else
		       { 	
			       String get_pwd = rs.getString(3);
			       
			       if(get_pwd.equals(password)) 
			       { 
			    	   myname=rs.getString(1);
			    	   get_count = rs.getInt(5);
				       aeskey=rs.getString(7);
				       mygrouplist=rs.getString(10);
				       uscode = "@" + rs.getInt(6);
				       
				       if(!mygrouplist.equals("NULL"))
				       {
					       StringTokenizer st = new StringTokenizer(mygrouplist, ":");
	
					       int county = 0;
					       while(st.hasMoreTokens())
					       {
					    	  gpcode.add(st.nextToken().trim());
					    	  
					    	   ResultSet rsgp=db.Query("select * from grouplist where gpcode like "+Integer.parseInt(gpcode.get(county).substring(1))+";");
					    	   rsgp.next();
					    	   
					    	   county++;				    	 
					    	   gpname.add(rsgp.getString(3));
					       }
					       groupcount[0] = (byte)(gpcode.size());
				       }
				       Server_Client_Manager.getInstance().login(_activity.getClientCode(), uscode);
			    	   
				       _checkLogin[0]=2;
			    	   if(get_count==1)
			    	   {
			    		   _checkLogin[1]=1;
			    		   db.Update("update test set count=2 where id='"+id+"';");
			    	   }
			       }
			       else 	
			       {
			    	   _checkLogin[0]=3; 
			       }   
		       }
		       _activity.send.setPacket(_checkLogin).write();
		       if(_checkLogin[0]==2)
		       {
		    	   Charset cs =Charset.forName("UTF-8");
		    	   
			       _activity.send.setPacket(groupcount,100).write();
			       _activity.send.setPacket(cs.encode(myname).array(),500).write();
			       _activity.send.setPacket(uscode.getBytes(), 100).write();
			       _activity.send.setPacket(aeskey.getBytes(), 500).write();
		       
				    for(int i =0; i < gpcode.size(); i++)
				    {
				   	 _activity.send.setPacket(gpcode.get(i).getBytes(), 100).write();
				   	 _activity.send.setPacket(gpname.get(i).getBytes(), 500).write();
				    }
		       }
		    }
		    catch(SQLException e1)
		    {
		       e1.printStackTrace();
		    }         
		}
	}
}
