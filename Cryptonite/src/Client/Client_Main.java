package Client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;


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
				Charset charSet = Charset.forName("UTF-8");
				ByteBuffer test = ByteBuffer.allocateDirect(100);
				ccs = Client_Server_Connector.getInstance(4444);
				/*ccs.configurePacket("A");
				test = charSet.encode("test");
				ccs.setPacket("A", test);
				ccs.send("A");*/
				ccs.justSend();
			}
			catch (InterruptedException e)
			{
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			} /*catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			while(true)
			{
				Thread.sleep(1);
			}
	       
	}
}
