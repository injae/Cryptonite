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
	
	private int _limit_size = 5;

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
			configurePacket("receive");
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
			try 
			{
				_buffer = ByteBuffer.allocateDirect(1024);
				_channel.read(_buffer);
				_buffer.flip();
				_packetList.get("receive").offer(_buffer);
			}
			catch (IOException e) 
			{
				System.exit(1);
				e.printStackTrace();
			}
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

	public void setPacket(String packetName, byte[] array)
	{
		byte[] buf = new byte[1024];
		for(int i =0; i < array.length; i++)
		{
			buf[i] = array[i];
		}

		if(_packetList.get(packetName).size() >= _limit_size)
		{
			sendNotRemove(packetName);
		}		
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		buffer.put(buf);
		buffer.flip();
		_packetList.get(packetName).offer(buffer);
	}
	
	public void setPacket(String packetName, ByteBuffer buffer)
	{
		if(_packetList.get(packetName).size() >= _limit_size)
		{
			sendNotRemove(packetName);
		}		
		_packetList.get(packetName).offer(buffer);
	}

	public void configurePacket(String packetName)
	{
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO 자동 생성된 catch 블록
			e1.printStackTrace();
		}
		_packetList.put(packetName, new LinkedList<ByteBuffer>());
	}

	public ByteBuffer receiveByteBuffer()
	{
		for(int i = 0; i < 1000; i++)
		{
			while(_packetList.get("receive").isEmpty())
			{
				try 
				{
					Thread.sleep(1);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return _packetList.get("receive").remove();
	}
	
	public byte[] receiveByteArray() throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		
		while(_packetList.get("receive").isEmpty())
		{
			try 
			{
				Thread.sleep(1);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		buffer = _packetList.get("receive").remove();
		byte[] temp = new byte[buffer.remaining()];
		buffer.get(temp, 0, temp.length);
		
		return temp;
	}

	public void sendNotRemove(String packetName)
	{
		Queue<ByteBuffer> output = _packetList.get(packetName);
		
		for(int i = 0; i < _limit_size; i++)
		{
			try 
			{
				_channel.write(output.remove());
			}
			catch (IOException e) 
			{
				System.exit(1);
				e.printStackTrace();
			}
		}
	}
	
	public void send(String packetName)
	{
		Queue<ByteBuffer> output = _packetList.get(packetName);
		
		while(!output.isEmpty())
		{
			try 
			{
				_channel.write(output.remove());
			}
			catch (IOException e) 
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
	
	public void sendAllNotRemove(String packetName)
	{
		Queue<ByteBuffer> output = _packetList.get(packetName);
		
		while(!output.isEmpty())
		{
			try 
			{
				_channel.write(output.remove());
			}
			catch (IOException e) 
			{
				System.exit(1);
				e.printStackTrace();
			}
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
			System.out.println("커넥션 오버");
		}
	}

	public void justSend() throws IOException
	{
		_buffer = ByteBuffer.allocateDirect(1024);
		byte[] buf = new byte[1024];
		buf[0] = 5;
		buf[1] = 15;
		
		_buffer.put(buf);
		_buffer.flip();
		_channel.write(_buffer);   
	}

	public void stopThread()
	{
		stopFlag = true;
	}

}