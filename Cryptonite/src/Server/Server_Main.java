package Server;

public class Server_Main 
{
	public static void main(String[] args)
	{
		Server_Client_Accepter SCA = new Server_Client_Accepter(4444);
		
		SCA.start();
	}
}
