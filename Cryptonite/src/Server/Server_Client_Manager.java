package Server;

import java.util.*;

import Function.PacketRule;

public class Server_Client_Manager implements PacketRule
{	
	private static Server_Client_Manager _client_manager;
	
	private HashMap<Integer ,Server_Client_Activity> _clientList;	
	private Queue<Integer> _usableClientCode;
	
	private int _lastClientCode;
	private Queue<Integer> _runningQueue;
	
	private Server_Client_Manager() 
	{
		_clientList = new HashMap<Integer, Server_Client_Activity>();
		_runningQueue = new LinkedList<Integer>();
		_usableClientCode = new LinkedList<Integer>();
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
		if(_usableClientCode.isEmpty()) { return ++_lastClientCode; }
		else { return _usableClientCode.remove(); }
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
			
		switch(packet[0])
		{
		case AUTOBACKUP:
			activity._funtionList.add(new Server_AutoBackup());
			activity._funtionList.getLast().Checker(packet); break;
		case LOGIN:
			activity._funtionList.add(new Server_Login());
			activity._funtionList.getLast().Checker(packet); break;
		case FILE_SHARE_RECEIVE:
			activity._funtionList.add(new Server_FileShare_Receive());
			activity._funtionList.getLast().Checker(packet); break;
		case FILE_SHARE_SEND:
			activity._funtionList.add(new Server_FileShare_Send());
			activity._funtionList.getLast().Checker(packet); break;
		case SIGN_UP:
			activity._funtionList.add(new Server_SignUp());
			activity._funtionList.getLast().Checker(packet); break;
		}
	}
	
	public void stopManaging(int clientCode)
	{
		if(_clientList.get(clientCode)._receiveQueue.isEmpty()) 
		_clientList.remove(clientCode);
		_usableClientCode.offer(clientCode);
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
