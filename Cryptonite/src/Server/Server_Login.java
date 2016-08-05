package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import Client.Client_Login;

public class Server_Login extends Server_Funtion
{
	Server_DataBase _db = Server_DataBase.getInstance();
	@Override
	public void Checker(byte[] packet) 
	{
		// TODO 자동 생성된 메소드 스텁
		_packetMaxCount=packet[1];
		
	}

	@Override
	public void running(Server_Client_Activity activity) {
		// TODO 자동 생성된 메소드 스텁
		
	String id = new String( activity._receiveQueue.remove()).trim();
	String password= new String( activity._receiveQueue.remove()).trim();
    
	System.out.println(id);
	
	byte[] _checkLogin=new byte[1024];
	try
    {
       ResultSet _rs  = _db.Query("select * from test where id like '"+ id +"';");
       
       if(!_rs.next()) 
       {	
    	   _checkLogin[0]=1; 
       }
       else
       { 	
	       String _get_pwd = _rs.getString(3);// 두번째 필드의 데이터
	       
	       if(_get_pwd.equals(password)){
	    	   _checkLogin[0]=2;
	       }
	       else{
	    	   _checkLogin[0]=3;
	       }   
       }
       System.out.println(_checkLogin[0]);
       activity.Sender(_checkLogin);
       activity.send();
       System.out.println("보냄");
    }
    catch(SQLException e1)
    {
       e1.printStackTrace();
    }         

	}
}
