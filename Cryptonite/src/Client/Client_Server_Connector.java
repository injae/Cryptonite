package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class Client_Server_Connector extends Thread
{
	private static Client_Server_Connector _singleton = null;

	private SocketChannel _channel;
	private ByteBuffer _buffer = null;
	private HashMap<String,Queue<ByteBuffer>> _packetList = null;
	private Vector<String> _packetNameList = null;
	private boolean stopFlag = false;

	private Client_Server_Connector(int port) throws InterruptedException
	{
		try 
		{		
			_channel = SocketChannel.open();
			_channel.configureBlocking(true);
			_channel.connect(new InetSocketAddress("localhost", port));

			while (!_channel.finishConnect()) 
			{
				Thread.sleep(1);
				System.out.println("still connecting");
			}

			_packetList = new HashMap<String,Queue<ByteBuffer>>();
			_packetNameList = new Vector<String>();
			_packetNameList.add("receive");
			_packetList.put("receive", new LinkedList<ByteBuffer>());
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		while(!stopFlag)
		{
			_packetList.get("receive").offer(_buffer);
		}
	}

	public static Client_Server_Connector getInstance(int port) throws InterruptedException
	{
		if(_singleton == null)
		{
			_singleton = new Client_Server_Connector(port);
		}

		return _singleton;
	}

	public void getPacket(String packetName, int bufferSize)
	{ 
		try 
		{
			_packetList.put(packetName, new LinkedList<ByteBuffer>());
			_packetNameList.add(packetName);
			_buffer = ByteBuffer.allocateDirect(bufferSize);
			int byteCount;

			_buffer.clear();
			while((byteCount = _channel.read(_buffer)) != -1)
			{
				if(byteCount < bufferSize)
				{
					_buffer = ByteBuffer.allocateDirect(byteCount);
				}
				else
				{
					_buffer = ByteBuffer.allocateDirect(bufferSize);
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
		_packetList.get(packetName).offer(buffer);
	}

	public void configurePacket(String packetName)
	{
		_packetList.put(packetName, new LinkedList<ByteBuffer>());
	}

	public ByteBuffer receive()
	{
		return _packetList.get("receive").remove();
	}

	public void send(String packetName)
	{
		Queue<ByteBuffer> output = _packetList.get(packetName);
		
		while(!output.isEmpty())
		{
			try 
			{
				Thread.sleep(10);
				_channel.write(output.remove());
			}
			catch (IOException | InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		_packetList.remove(packetName);
	}

	public void sendAll()
	{
		while(!_packetNameList.isEmpty())
		{
			send(_packetNameList.get(0));
			_packetNameList.remove(0);
		}
	}

	public void stopConnection()
	{
		try 
		{
			_channel.close();
		} 
		catch (IOException e)
		{
			System.out.println("Ä¿³Ø¼Ç ¿À¹ö");
		}
	}

	public void justSend() throws IOException
	{
		_buffer = ByteBuffer.allocateDirect(100);
		byte[] buf = new byte[100];
		buf[0] = 5;
		buf[1] = 8;
		
		_buffer.put(buf);
		_buffer.flip();
		_channel.write(_buffer);   
	}

	public void stopThread()
	{
		stopFlag = true;
	}

}