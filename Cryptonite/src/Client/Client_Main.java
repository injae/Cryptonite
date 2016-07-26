package Client;

import java.io.IOException;


/**
 * @author user
 *
 */
public class Client_Main
{
	public static void main(String[] args) throws InterruptedException
	{
			Client_Server_Connector  ccs;
			try 
			{ 
				ccs = new Client_Server_Connector (4444);
			} catch (InterruptedException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}
			while(true)
			{
				Thread.sleep(1);
			}
	       
	}
}
