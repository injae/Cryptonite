package Server;

import java.io.IOException;

/*
 * @author In Jae Lee
 * 
 */

public class Server_Main 
{
	public static void main(String[] args) throws IOException
	{
		new Server_Client_Accepter("localhost", 4444).start();
	}
} 
