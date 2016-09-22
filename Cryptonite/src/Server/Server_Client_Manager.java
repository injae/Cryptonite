package Server;

import java.io.IOException;
import java.util.*;

import Function.PacketRule;

/*
 * @author In Jae Lee
 * if you add new function show Server_Function_Factory
 */

public class Server_Client_Manager implements PacketRule
{	
	private static Server_Client_Manager _client_manager;
	public Server_Code_Manager _code_manager;
	
	private HashMap<String ,Server_Client_Activity> _clientList;	
	private Queue<String> _runningQueue;
	
	private Server_Client_Manager() 
	{
		_clientList = new HashMap<String, Server_Client_Activity>();
		_runningQueue = new LinkedList<String>();
		_code_manager = Server_Code_Manager.getInstance();
	}
	
	public static Server_Client_Manager getInstance()
	{
		if(_client_manager == null)
		{
			_client_manager = new Server_Client_Manager();
		}
		return _client_manager;		
	}
	
	public void login(String acCode ,String usCode)
	{
		_clientList.put(usCode, _clientList.remove(acCode));
		_clientList.get(usCode).setClientCode(usCode);
		_code_manager.removeCode(acCode);
		 Server_Administrator.getInstance().userUpdate("Login : "+ usCode);
	}
	
	public void logOut(String usCode)
	{
		String code = _code_manager.getAcCode();
		_clientList.put(code, _clientList.remove(usCode));
		_clientList.get(code).setClientCode(code);
		Server_Administrator.getInstance().userUpdate("Logout : "+ usCode);
	}
	
	public void register(String code, Server_Client_Activity server_client_activity)
	{
		_clientList.put(code, server_client_activity);
	}
	
	public int HowManyClient()
	{
		return _clientList.size();
	}
	
	public void requestManage(String clientCode)
	{
		_runningQueue.offer(clientCode);		
	}
	
	public void stopManaging(String clientCode)
	{
		if(_clientList.get(clientCode).receive.isEmpty()) 
		{
			Server_Client_Activity activity = _clientList.get(clientCode);
			if(_code_manager.isAcCode(clientCode))
			{
				_clientList.remove(clientCode);
				_code_manager.removeCode(clientCode);
			}
			else
			{
				logOut(clientCode);
				String code = activity.getClientCode();
				_clientList.remove(code);
				_code_manager.removeCode(code);
			}
			activity.close();
		}
		else
		{
			_clientList.get(clientCode)._funtionList.addLast(Server_Function_Factory.create((byte)-1,_clientList.get(clientCode)));
		}
	}
	
	public void setEvent(String target, byte[] event)
	{
		if(_clientList.get(target) != null)
		{
			_clientList.get(target)._eventQueue.offer(event);
		}
		System.out.println("event -> "+target);
	}
	
	public void run() throws IOException
	{	
		Server_Client_Activity activity = null;

		while(!_runningQueue.isEmpty())
		{
			activity = _clientList.get(_runningQueue.remove());	
			activity._funtionList.getFirst().running(activity.getPakcetCount());
			activity.finishCheck();
		}
	}
}
