package Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Function.PacketProcessor;
import Function.PacketRule;
import Function.SecurePacketProcessor;

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
	
	public Queue<byte[]> _eventQueue;
	private byte[] _receiveEvent;
	
	private int _packetCount = 0;
	
	public LinkedList<Server_Funtion> _funtionList;
	
	public SecurePacketProcessor receive;
	public SecurePacketProcessor send;
	
	//Aes key for Communication
	private SecretKey comKey;

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

            _eventQueue = new LinkedList<byte[]>();
            _funtionList = new LinkedList<Server_Funtion>();
            
            _manager = Server_Client_Manager.getInstance();

            //System.out.println(_channel.toString() + "connect");
            
            receive = new SecurePacketProcessor(_channel, true);
            send = new SecurePacketProcessor(_channel, true); 
            
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
	
	public void Receiver() throws Exception 
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
			else if(_funtionList.getLast().isCut(_packetCount))
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
		//System.out.println(_channel.toString() + "Stop Connect");
		try {
			_channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SecretKey getFileKey()
	{
		Server_DataBase db = Server_DataBase.getInstance();
		
		ResultSet rs = db.Query("Select *from test where uscode = "+ Server_Code_Manager.codeCutter(getClientCode())+";");
		String aeskey = null;
		try {
			rs.next();
			aeskey = rs.getString(7);
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		return new SecretKeySpec(Base64.getDecoder().decode(aeskey), "AES");
	}
	
	public void setKey(SecretKey key){
		comKey = key;
	}
	public SecretKey getKey(){
		return comKey;
	}
}
