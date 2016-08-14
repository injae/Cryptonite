package Function;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import Function.PacketProcessor;


public class Client_Server_Connector extends Thread
{
	private static Client_Server_Connector _singleton = null;

	private SocketChannel _channel;
	public PacketProcessor receive;
	public PacketProcessor send;

	private Client_Server_Connector()
	{
		try
		{
			_channel = SocketChannel.open();
			_channel.configureBlocking(true);
			_channel.connect(new InetSocketAddress("192.168.0.10", 4444));

		/*	while (!_channel.finishConnect())
			{
				Thread.sleep(1);
				System.out.println("still connecting");
			}*/

			receive = new PacketProcessor(_channel, false);
			send = new PacketProcessor(_channel, false);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	public static Client_Server_Connector getInstance()
	{
		if(_singleton == null)
		{
			_singleton = new Client_Server_Connector();
		}

		return _singleton;
	}
}