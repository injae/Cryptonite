package Server;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Server_SignUp extends Server_Funtion 
{
	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = packet[1];
	}

	@Override
	public void running(Server_Client_Activity activity) 
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
		
		Server_DataBase db;
		db=Server_DataBase.getInstance();
		db.Init_DB("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/"+"cryptonite", "root", "yangmalalice3349!");
		db.connect();
		db.Update("INSERT INTO TEST VALUES("+"'"+name+"','"+id+"','"+password+"','"+email+"','"+count+"');");
	}
}
