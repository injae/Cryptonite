package Server;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

import org.omg.CORBA.ACTIVITY_COMPLETED;

import Function.PacketRule;

/*
 * @author In Jae Lee
 * if you add new function show Server_Function_Factory
 */

public class Server_Client_Manager implements PacketRule
{	
	private static Server_Client_Manager _client_manager;
	public Server_UserCode_Manager _code_manager;
	
	private HashMap<String ,Server_Client_Activity> _clientList;	
	private Queue<String> _runningQueue;

	private Server_Client_Manager() 
	{
		_clientList = new HashMap<String, Server_Client_Activity>();
		_runningQueue = new LinkedList<String>();
		_code_manager = Server_UserCode_Manager.getInstance();
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
		System.out.println("Login: "+usCode);
	}
	
	public void logOut(String usCode)
	{
		_clientList.put(_code_manager.getAcCode(),_clientList.remove(usCode));
		System.out.println("Logout: "+usCode);
	}
	
	public void register(String code, Server_Client_Activity server_client_activity)
	{
		_clientList.put(code, server_client_activity);
		System.out.println("How many Client " + _clientList.size());
		System.out.println("New Client: "+code);
	}
	
	public void requestManage(String clientCode)
	{
		_runningQueue.offer(clientCode);		
	}
	
	public void packetChecker(Server_Client_Activity activity)
	{			
		byte[] packet = activity.receive.getByte();
		
		activity._funtionList.add(Server_Function_Factory.create(packet[0]));
		activity._funtionList.getLast().Checker(packet,activity);
	}
	
	public void stopManaging(String clientCode)
	{
		if(_clientList.get(clientCode).receive.isEmpty()) 
		{
			_clientList.remove(clientCode);
			_code_manager.removeUsCode(clientCode);
		}
		else
		{
			_clientList.get(clientCode)._funtionList.addLast(Server_Function_Factory.create((byte)0));
			_client_manager._runningQueue.offer(clientCode);
		}
		System.out.println("Exit Client: "+clientCode);
	}
	
	public void run()
	{	
		Server_Client_Activity activity = null;
		try 
		{
			while(!_runningQueue.isEmpty())
			{
				activity = _clientList.get(_runningQueue.remove());	
				activity.readableUpdate();
				activity._funtionList.getFirst().running();
				activity.finishCheck();
			}
		} 
		catch (IOException e) 
		{
			stopManaging(activity.getClientCode());
			e.printStackTrace();
		}
	}
}
