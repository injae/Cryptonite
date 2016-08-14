package Client;

/**
 * @author user
 *
 */
public class Client_Main
{
	public static void main(String[] args)
	{		
		Client_Server_Connector csc = Client_Server_Connector.getInstance();
			
		Client_FolderScan cfs = new Client_FolderScan("C:\\Server");
		cfs.start();
		
		Client_AutoBackup cab = new Client_AutoBackup();
		cab.start();
		
		/*Client_FileShare_Send cfs = new Client_FileShare_Send();
		cfs.click();*/
		
		/*Client_FileShare_Receive cfr = new Client_FileShare_Receive();
		cfr.receiveFiles("682519");*/
		
		//Client_Login cl = new Client_Login();
	}
}
