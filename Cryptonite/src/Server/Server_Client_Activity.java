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

import Function.PacketProcessor;
import Function.PacketRule;

/*
 * @author In Jae Lee
 * 
 */

public class Server_Client_Activity implements PacketRule 
{
	public final static int LIMIT_PACKET = 10;
	
	private SocketChannel _channel;
	private Server_Client_Manager _manager;
	private String _clientCode;
	
	private Queue<Integer> _readableQueue;
	private Queue<byte[]> _eventQueue;
	private byte[] _receiveEvent;
	
	private int _packetCount = 0;
	private int _usedCount = 1;

	public LinkedList<Server_Funtion> _funtionList;
	
	public PacketProcessor receive;
	public PacketProcessor send;
	
	public Integer _readableCount = 0;
	public int _readingCount = 0;


	public Server_Client_Activity(Selector selector, SelectionKey key, String clientCode)
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
            
            _readableQueue = new LinkedList<Integer>();
            _eventQueue = new LinkedList<byte[]>();
            _funtionList = new LinkedList<Server_Funtion>();
            
            _manager = Server_Client_Manager.getInstance();

            System.out.println(_channel.toString() + "connect");
            
            receive = new PacketProcessor(_channel, true);
            send = new PacketProcessor(_channel, true); 
            
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public byte[] getReceiveEvent()
	{
		return _receiveEvent;
	}
	
	public int getPakcetCount()
	{
		return _packetCount;
	}
	
	public void Receiver() throws IOException 
	{	
		receive.read();
		
		_packetCount++;
		
		if(_packetCount == 1)
		{
			_receiveEvent = receive.getByte();
			_funtionList.add(Server_Function_Factory.create(_receiveEvent[0],this));
			_manager.requestManage(_clientCode);
		}
		else
		{
			if(_funtionList.getLast()._packetMaxCount == _packetCount)
			{
				_manager.requestManage(_clientCode);
			}
			else if(_funtionList.getLast()._packetMaxCount > LIMIT_PACKET)
			{		
				_manager.requestManage(_clientCode);
			}
		}

	}
	
	public void finishCheck()
	{
		if(_packetCount == _funtionList.getFirst()._packetMaxCount)
		{
			_funtionList.removeFirst();
			_packetCount = 0;
		}
	}
	
	public String getClientCode() { return _clientCode; }
	public void setClientCode(String code) {_clientCode = code;}
	
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
