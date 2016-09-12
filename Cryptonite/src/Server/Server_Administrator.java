package Server;

import java.util.Scanner;

import Function.Timer;

public class Server_Administrator extends Thread
{
	private Timer timer;
	private static Server_Administrator instance;
	private int allpacketlength = 0;
	
	private Server_Administrator() 
	{
		timer = new Timer();
	}
	
	public static Server_Administrator getInstance()
	{
		if(instance == null)
		{
			instance = new Server_Administrator();
		}
		return instance;
	}
	

	public void run()
	{
		Scanner input = new Scanner(System.in);
		
		System.out.println("Server Running");
		System.out.println("Server Adimistrator Start");
		System.out.println("if you want to use command use help");
		
		while(true)
		{
			String command = input.nextLine();
			
			switch(command)
			{
			case "help":
				System.out.println("<command>");
				System.out.println("1. all packet size");
				System.out.println("2. how many user");
				System.out.println("3. running time");
				System.out.println("4. stop");
				break;
				
			case "all packet size":
				System.out.println("All Packet Size: "+allpacketlength);	 break;
				
			case "how many user":
				System.out.println("How many user: " +Server_Client_Manager.getInstance().HowManyClient());	 break;
				
			case "running time":
				long hour = timer.runningTime() / (3600 * 1000);
				
				long min  = timer.runningTime() / (60   * 1000);
				if(min >= 60){ min %= 60;}
				
				long sec = timer.runningTime()  / (		  1000);
				if(sec >= 60){ sec %= 60;}
				
				System.out.println("Running Time: "+hour+"h." +min + "m." + sec +"s"); break;
			case "stop":
				if(YorN()){ System.exit(1); }
			}
		
		}
		
	}
	
	public boolean YorN()
	{
		System.out.print("<y/n> ");
		String ans = new Scanner(System.in).nextLine();
		if(ans.equalsIgnoreCase("y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void updatePacketSize()
	{
		allpacketlength++;
	}
}
