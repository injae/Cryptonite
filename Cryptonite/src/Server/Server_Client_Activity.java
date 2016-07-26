package Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Queue;

public class Server_Client_Activity 
{
	private SocketChannel _channel;
	private Queue<byte[]> _receiveQueue; 
	private Queue<byte[]> _sendQueue;
	
	public Server_Client_Activity(Selector selector, SelectionKey key)
	{
		try 
		{
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
	        _channel = serverSocketChannel.accept();
	        
	        _channel.configureBlocking(false);
            SelectionKey clientKey = _channel.register(selector, 
            									SelectionKey.OP_READ,SelectionKey.OP_WRITE);
            clientKey.attach(this);
            
            _receiveQueue = new LinkedList<byte[]>();
            _sendQueue = new LinkedList<byte []>();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void Send(byte) 
	{
		 
	}
	
	public void Receiver() 
	{	
		try
		{
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);		
			_channel.read(buffer);						
			_receiveQueue.add(buffer.array());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
}
