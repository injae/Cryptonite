package Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import Function.PacketRule;

public class Server_Client_Activity implements PacketRule 
{
	private SocketChannel _channel;
	
	public Deque<byte[]> _receiveQueue; 
	public Queue<ByteBuffer> _sendQueue;
	
	private Queue<Integer> _readableQueue;
	
	private int _packetCount = 0;
	public Integer _readableCount = 0;
	public int _readingCount = 0;
	private int _usedCount = 1;
	
	private int _clientCode = 0;
	private int LIMIT_PACKET = 10;
	
	public Server_User_Info _loginInfo;	
	public LinkedList<Server_Funtion> _funtionList;
	
	public Server_Client_Manager _manager;
	
	public Server_Client_Activity(Selector selector, SelectionKey key, int clientCode)
	{
		try 
		{
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
	        _channel = serverSocketChannel.accept();
	        
	        _channel.configureBlocking(false);
            SelectionKey clientKey = _channel.register(selector, 
            									SelectionKey.OP_READ,SelectionKey.OP_WRITE);
            clientKey.attach(this);
            
            _clientCode = clientCode;
            
            _receiveQueue = new LinkedList<byte[]>();            
            _sendQueue = new LinkedList<ByteBuffer>();
            _readableQueue = new LinkedList<Integer>();
            
            _loginInfo = new Server_User_Info();
            _manager = Server_Client_Manager.getInstance();
            _funtionList = new LinkedList<Server_Funtion>();

            System.out.println(_channel.toString() + "connect");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public int getClientCode()
	{
		return _clientCode;
	}
	
	public void Sender(byte[] packet) 
	{
		if(_sendQueue.size() >= LIMIT_PACKET)
		{
			sendNotRemove();
		}		
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		buffer.put(packet);
		buffer.flip();
		_sendQueue.offer(buffer);
	}
	
	private void sendNotRemove() 
	{
		for(int i = 0; i < LIMIT_PACKET; i++)
		{
			try 
			{
				_channel.write(_sendQueue.remove());
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void send()
	{	
		while(!_sendQueue.isEmpty())
		{
			try 
			{
				_channel.write(_sendQueue.remove());
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}		
	}

	public void Receiver() throws IOException 
	{	
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);		
		int count = _channel.read(buffer);		
		
		buffer.flip();
		byte[] array = new byte[buffer.remaining()];
		buffer.get(array);
		
		_receiveQueue.add(array);
		_packetCount++;
		_readableCount++;
		
		//System.out.println(_channel.toString() + "read :" + count);	
		if(_packetCount == 1)
		{
			_readableCount--;
			_manager.packetChecker(this);
		}
		else
		{
			if(_funtionList.getLast()._packetMaxCount == _packetCount)
			{
				_readableQueue.offer(_readableCount);
				_packetCount = 0;
				_readableCount = 0;
				_manager.requestManage(_clientCode);
			}
			else if(_readableCount >= _funtionList.getLast().getLimitSize())
			{			
				_readableQueue.offer(_readableCount);
				_readableCount = 0;
				_manager.requestManage(_clientCode);
			}
		}
	}
	public void readableUpdate()
	{
		_readingCount = _readableQueue.remove();
		_usedCount +=  _readingCount;
	}
	
	public void finishCheck()
	{
		if(_usedCount == _funtionList.getFirst()._packetMaxCount)
		{
			_funtionList.removeFirst();
			_usedCount = 1;
			System.out.println("finish");
		}
	}
	
	public boolean IsReadable()
	{
		if(_readingCount > 0)
		{
			_readingCount--;
			return true;
		}
		else
		{
			return false;	
		}
	}
	
	public void close() 
	{
		System.out.println(_channel.toString() + "Stop Connect");
		try {
			_channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
