package Server;

import java.util.*;

import Function.PacketRule;

public class Server_Client_Manager implements PacketRule
{	
	private static Server_Client_Manager _server_client_manager;
	
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
		if(_server_client_manager == null)
		{
			_server_client_manager = new Server_Client_Manager();
		}
		return _server_client_manager;		
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
		byte[] packet = activity._receiveQueue.remove();
		
		switch(packet[0])
		{
		case AUTOBACKUP:
			activity._funtionList.get(AUTOBACKUP).Checker(packet); break;
		case LOGIN:
			activity._funtionList.get(LOGIN).Checker(packet); break;
		case FILE_SHARE_RECEIVE:
			activity._funtionList.get(FILE_SHARE_RECEIVE).Checker(packet); break;
		case FILE_SHARE_SEND:
			activity._funtionList.get(FILE_SHARE_SEND).Checker(packet); break;
		case SIGN_UP:
			activity._funtionList.get(SIGN_UP).Checker(packet); break;		
		}
		activity._runningFuntion.offer(packet[0]);
	}
	
	public void stopManaging(int clientCode)
	{
		_clientList.remove(clientCode);
		_usableClientCode.offer(clientCode);
	}
	
	public void run()
	{	

		while(!_runningQueue.isEmpty())
		{
			Server_Client_Activity activity = _clientList.get(_runningQueue.remove());	
			activity.readableUpdate();
			activity._funtionList.get(activity._runningFuntion.remove()).running(activity);
		}
	}
}
