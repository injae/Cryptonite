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
			
			for(int i =0; i < 3; i++)
			{
				ccs.justSend();						
			}			

		/*	while(true)
			{
				Thread.sleep(1);
			}*/
		}
		catch (InterruptedException | IOException e)
		{
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
   
	}
}
