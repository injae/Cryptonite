package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;

import javax.swing.JComboBox.KeySelectionManager;

import Function.Timer;

/*
 * @author In Jae Lee
 * 
 */

public class Server_Client_Accepter extends Thread
{
	private Selector _selector;
	private Server_Client_Manager _manager;
	
	public Server_Client_Accepter(String hostname ,int port)
	{
		 try 
		 {
			ServerSocketChannel channel = ServerSocketChannel.open();
			channel.bind(new InetSocketAddress(hostname, port)); 
	        channel.configureBlocking(false);
	        
	        _selector = Selector.open();	        
	        channel.register(_selector, SelectionKey.OP_ACCEPT);
	        
			Server_Administrator.getInstance().start();
	        _manager = Server_Client_Manager.getInstance();
	        Server_Thread_Manager.getInstance().start();
 		 }
		 catch (IOException e)
		 {
			e.printStackTrace();
		 }
	}
	
	public void run()
	{
		SelectionKey key = null;

		while(!Thread.interrupted())
		{	
			try 
			{
			    _manager.run();	
				if(_selector.selectNow() == 0) continue;
			    Iterator<SelectionKey> keys = _selector.selectedKeys().iterator();	
			    
			    while (keys.hasNext()) 
			    {
			        key = keys.next();
			        keys.remove(); 
			     
			        if(key.isValid())
			        {
				        if(key.isAcceptable())
				        {	
				        	String clientCode = _manager._code_manager.getAcCode();
				        	_manager.register(clientCode, new Server_Client_Activity(_selector, key, clientCode)); 	
				        }
				        else if(key.isReadable())
				        {			        	
				        	Server_Client_Activity activity = (Server_Client_Activity)key.attachment();
				        	activity.Receiver();	
				        }
			        }
			    }	
			}
			catch (Exception e) 
			{
				Server_Administrator.getInstance().errorUpdate(e.getMessage());
				Server_Client_Activity activity = (Server_Client_Activity)key.attachment();
				_manager.stopManaging(activity.getClientCode());
				key.cancel();
			}
        }        

	}
}
