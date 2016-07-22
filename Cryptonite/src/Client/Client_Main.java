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
	        Client_Connecter_Server ccs;
			try {
				ccs = new Client_Connecter_Server(4444);
				 ccs.SendByte(null, 0);
			} catch (InterruptedException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}
	       
	}
}
