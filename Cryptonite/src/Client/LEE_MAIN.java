package Client;

import Server.Server_DataBase;

public class LEE_MAIN 
{
	public static void main(String[] args) 
	{
		Server_DataBase DB = Server_DataBase.getInstance();
		DB.Init_DB("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/projectdb", "root", "0000");
		DB.connect();
	}
}
