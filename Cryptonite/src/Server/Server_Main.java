package Server;

import java.io.IOException;

public class Server_Main 
{
	public static void main(String[] args) throws IOException
	{
		Server_DataBase _db;
	    _db=Server_DataBase.getInstance();
	    _db.Init_DB("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/"+"cryptonite", "root", "yangmalalice3349!");
	    _db.connect();
		
		 Server_Client_Accepter sca = new Server_Client_Accepter("localhost", 4444);
		 sca.start();
	}
}
