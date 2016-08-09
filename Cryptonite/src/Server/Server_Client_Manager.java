package Server;

import java.util.*;

import Function.PacketRule;

/*
 * @author In Jae Lee
 * if you add new function show Server_Function_Factory
 */

public class Server_Client_Manager implements PacketRule
{	
	private static Server_Client_Manager _client_manager;
	private Server_DataBase _db;
	
	private HashMap<Integer ,Server_Client_Activity> _clientList;	
	private Queue<Integer> _usableActivityCode;
	private Queue<Integer> _runningQueue;
	private int _lastClientCode;

	private Server_Client_Manager() 
	{
		_clientList = new HashMap<Integer, Server_Client_Activity>();
		_runningQueue = new LinkedList<Integer>();
		_usableActivityCode = new LinkedList<Integer>();
		
		_db = Server_DataBase.getInstance();
	}
	
	public static Server_Client_Manager getInstance()
	{
		if(_client_manager == null)
		{
			_client_manager = new Server_Client_Manager();
		}
		return _client_manager;		
	}
	
	public int getClientCode()
	{
		if(_usableActivityCode.isEmpty()) { return ++_lastClientCode; }
		else 							  { return _usableActivityCode.remove(); }
	}
	
	public void register(int key, Server_Client_Activity server_client_activity)
	{
		_clientList.put(key, server_client_activity);
		System.out.println("How many Client " + _clientList.size());
	}
	
	public void requestManage(int clientCode)
	{
		_runningQueue.offer(clientCode);		
	}
	
	public void packetChecker(Server_Client_Activity activity)
	{		
		byte[] packet = activity._receiveQueue.removeLast();		
		
		activity._funtionList.add(Server_Function_Factory.create(packet[0]));
		activity._funtionList.getLast().Checker(packet);		
	}
	
	public void stopManaging(int clientCode)
	{
		if(_clientList.get(clientCode)._receiveQueue.isEmpty()) 
		_clientList.remove(clientCode);
		_usableActivityCode.offer(clientCode);
	}
	
	public void run()
	{	
		while(!_runningQueue.isEmpty())
		{
			Server_Client_Activity activity = _clientList.get(_runningQueue.remove());	
			activity.readableUpdate();
			activity._funtionList.getFirst().running(activity);
			activity.finishCheck();
		}
	}
}
