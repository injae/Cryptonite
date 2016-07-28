package Client;

import java.io.IOException;

/**
 * @author user
 *
 */
public class Client_Main
{
	public static void main(String[] args) throws InterruptedException, IOException
	{		
		Client_Server_Connector ccs = Client_Server_Connector.getInstance(4444);
				
		//Client_SignUp csu = new Client_SignUp();
		
		while(true)
		{
			for(int i = 0 ; i< 100; i++)
			{
				Thread.sleep(10);
				ccs.justSend();	
			}
		}
	}
}
