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
				
		Client_SignUp csu = new Client_SignUp();
		Client_SignUp csd = new Client_SignUp();
		
		/*for(int i = 0 ; i< 8; i++)
		{
			Thread.sleep(10);
			ccs.justSend();	
		}*/
		
	/*	while(true)
		{
			Thread.sleep(1);
		}*/
	}
}
