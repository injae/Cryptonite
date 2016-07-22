package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class Client_Server_Connector 
{
	private SocketChannel _channel;
	private ByteBuffer _buffer = null;
	private HashMap<String,Queue<ByteBuffer>> _packetList = null;
	private Vector<String> _packetNameList = null;
	//
	
	public Client_Server_Connector(int port) throws InterruptedException
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
			
			_packetList = new HashMap<String,Queue<ByteBuffer>>();
			_packetNameList = new Vector<String>();
        } 
        catch (IOException e)
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
	
	public void getPacket(String packetName)
	{	
		try 
		{
			_packetList.put(packetName, new LinkedList<ByteBuffer>());
			_packetNameList.add(packetName);
			_buffer = ByteBuffer.allocateDirect(1024);
			int byteCount;
			
			_buffer.clear();
			while((byteCount = _channel.read(_buffer)) != -1)
			{
				if(byteCount < 1024)
				{
					_buffer = ByteBuffer.allocateDirect(byteCount);
				}
				else
				{
					_buffer = ByteBuffer.allocateDirect(1024);
				}
				_buffer.flip();
				_packetList.get(packetName).offer(_buffer);
				_buffer.clear();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void setPacket(String packetName, ByteBuffer buffer)
	{
		_packetList.put(packetName, new LinkedList<ByteBuffer>());
		_packetList.get(packetName).offer(buffer);
	}
	
	public Queue<ByteBuffer> receive(String packetName)
	{
		Queue<ByteBuffer> temp = _packetList.get(packetName);
		_packetList.remove(packetName);
		return temp;
	}
	
	public void send(String packetName)
	{
		Queue<ByteBuffer> output = _packetList.get(packetName);
		
		while(!output.isEmpty())
		{
			try 
			{
				_channel.write(output.poll());
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void sendAll()
	{
		while(!_packetNameList.isEmpty())
		{
			send(_packetNameList.get(0));
			_packetNameList.remove(0);
		}
	}
}
