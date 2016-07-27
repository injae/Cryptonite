package Client;

import java.io.IOException;


/**
 * @author user
 *
 */
public class Client_Main
{
	public static void main(String[] args)
	{		
		try 
		{ 
			Client_Server_Connector ccs = Client_Server_Connector.getInstance(4444);
					
			ccs.justSend();	
		/*	while(true)
			{
				Thread.sleep(1);
				ccs.justSend();	
			}*/
		}
		catch (InterruptedException | IOException e)
		{
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
   
	}
}
