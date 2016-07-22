package Server;

import java.io.IOException;

public class Server_Main 
{
	
	public static void main(String[] args) throws IOException
	{
		 Server_Client_Accepter sca = new Server_Client_Accepter("localhost", 4444);
		 sca.start();
	}
}
