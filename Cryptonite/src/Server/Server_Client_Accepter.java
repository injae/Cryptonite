package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

public class Server_Client_Accepter extends Thread
{
	private String clientChannel = "clientChannel";
	private String serverChannel = "serverChannel";
	private String channelType = "channelType";
	
	private ServerSocketChannel _serverChannel = null;
	private InetSocketAddress _address = null;
	private ArrayList<SocketChannel> _socketList;
	private Selector _selector = null;	
	private SelectionKey _socketServerSelectionKey;
	
	private Set<SelectionKey> _selectedKeys;
	
	public Server_Client_Accepter(int port)
	{
		try 
		{
			_serverChannel = ServerSocketChannel.open();
			_address = new InetSocketAddress(port);
			_serverChannel.socket().bind(_address);
			_serverChannel.configureBlocking(false);
			
			_selector = Selector.open();
			_socketServerSelectionKey = _serverChannel.register(_selector, SelectionKey.OP_ACCEPT);
			
			Map<String, String> properties = new HashMap<String, String>();
			properties.put(channelType, serverChannel);
			_socketServerSelectionKey.attach(properties);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		while(true)
		{
			try 
			{
				if(_selector.select() == 0) { continue; }
				_selectedKeys = _selector.selectedKeys();
				Iterator<SelectionKey> iterator = _selectedKeys.iterator();
				
				while(iterator.hasNext())
				{
					SelectionKey key = iterator.next();
					
					if(((Map<String,String>)key.attachment()).get(channelType).equals(serverChannel));
					{
						ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
						SocketChannel clientSocketChannel = serverSocketChannel.accept();
						
						if(clientSocketChannel != null)
						{
							clientSocketChannel.configureBlocking(false);
							SelectionKey clientKey = clientSocketChannel.register(
													_selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE );
							Map<String,String> clientproperties = new HashMap<String, String>();
							clientproperties.put(channelType, clientChannel);
							clientKey.attach(clientproperties);
							
							CharBuffer buffer = CharBuffer.wrap("Hello Client");
							while(buffer.hasRemaining())
							{
								clientSocketChannel.write(Charset.defaultCharset().encode(buffer));
							}
							buffer.clear();
						}
						else
						{
							ByteBuffer buffer = ByteBuffer.allocateDirect(20);
							SocketChannel clientChannel =(SocketChannel)key.channel();
							int bytesRead = 0;
							if(key.isReadable())
							{
								if((bytesRead = clientChannel.read(buffer)) > 0)
								{
									buffer.flip();
									System.out.println(Charset.defaultCharset().decode(buffer));
									buffer.clear();
								}
								if(bytesRead < 0)
								{
									clientChannel.close();
								}
							}
						}
					}
					
				}
				iterator.remove();
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
