package Server;

import java.io.IOException;

/*
 * @author In Jae Lee
 * 
 */

public class Server_Main 
{
	public static void main(String[] args)
	{
		new Server_Client_Accepter("192.168.0.10", 4444).start();
	}
} 
