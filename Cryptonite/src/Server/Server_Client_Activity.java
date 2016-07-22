package Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Server_Client_Activity 
{
	SocketChannel _channel;
	
	public Server_Client_Activity()
	{
		
	}

	public  void NewClient(Selector selector, SelectionKey key)
	{
		try 
		{
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
	        _channel = serverSocketChannel.accept();
	       
	        _channel.configureBlocking(false);
            SelectionKey clientKey = _channel.register(selector, 
            									SelectionKey.OP_READ,SelectionKey.OP_WRITE);
            clientKey.attach(new Server_Client_Activity()); 
            
            Send();
            Receiver();
            
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void Send() throws IOException
	{
		  CharBuffer buffer = CharBuffer.wrap("Hello client");
          while (buffer.hasRemaining()) {
              _channel.write(Charset.defaultCharset()
                      .encode(buffer));
          }
          buffer.clear();
	}
	
	public void Receiver() throws IOException
	{
		 ByteBuffer buffer = ByteBuffer.allocate(20);
		 int bytesRead = 0;
         if ((bytesRead = _channel.read(buffer)) > 0) {
             buffer.flip();
             System.out.println(Charset.defaultCharset().decode(
                     buffer));
             buffer.clear();
         }
         if (bytesRead < 0) {

         }
	}
	
}
