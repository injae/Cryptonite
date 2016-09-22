package Server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.omg.stub.java.rmi._Remote_Stub;

public class Server_Thread_Manager extends Thread
{
	static private Server_Thread_Manager manager;
	
	private Queue<Server_Funtion_Thread> runningQueue;
	
	private Server_Thread_Manager() 
	{
		runningQueue = new LinkedBlockingQueue<>();
	}

	static public Server_Thread_Manager getInstance()
	{
		if(manager == null)
		{
			manager = new Server_Thread_Manager();
		}
		
		return manager;
	}
	
	public void register(Server_Funtion_Thread thread)
	{
		runningQueue.add(thread);
	}
	
	public void run()
	{
		while(true)
		{
			if(!runningQueue.isEmpty())
			{
				runningQueue.remove().start();
			}
		}
	}
	
}
