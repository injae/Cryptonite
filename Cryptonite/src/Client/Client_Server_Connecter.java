package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client_Server_Connecter 
{
	private SocketChannel _channel;
	
	public Client_Server_Connecter(int port) throws InterruptedException
	{
        try 
        {
			_channel = SocketChannel.open();
			_channel.configureBlocking(false);
			_channel.connect(new InetSocketAddress("localhost", port));
			
			while (!_channel.finishConnect()) 
			{
				 Thread.sleep(1);
	             System.out.println("still connecting");
			}
			System.out.println("?");
        } catch (IOException e)
        {
			e.printStackTrace();
		}
	}
	
	public void SendByte(byte[] sendbyte, int size) throws IOException
	{
		 ByteBuffer bufferA = ByteBuffer.allocate(20);
         int count = 0;
         String message = "";
         while ((count = _channel.read(bufferA)) > 0) {
             // flip the buffer to start reading
             bufferA.flip();
             message += Charset.defaultCharset().decode(bufferA);

         }
     	System.out.println("보낸다");
         if (message.length() > 0) {
             System.out.println(message);
             // write some data into the channel
             CharBuffer buffer = CharBuffer.wrap("Hello Server");
             while (buffer.hasRemaining()) {
                 _channel.write(Charset.defaultCharset().encode(buffer));
             }
             message = "";
         }
     	System.out.println("받았다");
	}	
}
