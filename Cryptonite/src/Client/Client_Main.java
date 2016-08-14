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
		/*Client_Server_Connector csc = Client_Server_Connector.getInstance();*/
			
		/*Client_AutoBackup cab = new Client_AutoBackup();
		cab.start();*/
		
		Client_FileShare_Send cfs = new Client_FileShare_Send();
		cfs.click();
		
		/*Client_FileShare_Receive cfr = new Client_FileShare_Receive();
		cfr.receiveFiles();*/
		
		//Client_Login cl = new Client_Login();

/*		while(true)
		{
			Thread.sleep(10);
		}*/
		
	}
}
