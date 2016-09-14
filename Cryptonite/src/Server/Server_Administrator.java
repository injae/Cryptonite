package Server;

import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

import Function.Logger;
import Function.Timer;

public class Server_Administrator extends Thread
{
	private Timer timer;
	private static Server_Administrator instance;
	private int allpacketlength = 0;
	
	private Logger userInfo;
	
	private Server_Administrator() 
	{
		timer = new Timer();
		userInfo = new Logger();
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("<DataBase Login>");
		System.out.print("id : "); String id  	 = input.nextLine();
		System.out.print("password : "); String password = input.nextLine();
		
		Server_DataBase db=Server_DataBase.getInstance();
	    db.Init_DB("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/"+"cryptonite", id, password);
	    progressbar();
	    db.connect();
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
		
		System.out.println("=============================");
		System.out.println("* Welcome Cryptonite Server *");
		System.out.println("* Version : Beta 1.0.0      *");
		System.out.println("=============================");
		
		while(true)
		{
			System.out.print(">");
			String command = input.nextLine();
			
			switch(command)
			{
			case "help":
				System.out.println("<command>");
				System.out.println("  <command> :  <explain>");
				System.out.println("*   size    : size of all packet");
				System.out.println("*   user    : how many user count");
				System.out.println("*   time    : server running time");
				System.out.println("*   stop    : server stop");
				System.out.println("*   log -u  : users login logout log");
				break;
				
			case "size":
				System.out.println("All Packet Size: "+allpacketlength);	 break;
				
			case "user":
				System.out.println("How many user: " +Server_Client_Manager.getInstance().HowManyClient());	 break;
				
			case "time":
				long hour = timer.runningTime() / (3600 * 1000);
				
				long min  = timer.runningTime() / (60   * 1000);
				if(min >= 60){ min %= 60;}
				
				long sec = timer.runningTime()  / (		  1000);
				if(sec >= 60){ sec %= 60;}
				
				System.out.println("Running Time: "+hour+"h." +min + "m." + sec +"s"); break;
				
			case "log -u":
				
				System.out.println("=============================");
				System.out.println("**           Log           **");
				for(int i =0; i < userInfo.size(); i++)
				{
					System.out.println(userInfo.getLog(i));
				}
				System.out.println("=============================");
				break;
				
			case "stop":
				if(YorN()){ System.exit(1); }
			}
		}
	}
	
	public void userUpdate(String str)
	{
		userInfo.update(str);
	}
	
	public boolean YorN()
	{
		System.out.print("<y/n> ");
		String ans = new Scanner(System.in).nextLine();
		
		return ans.equalsIgnoreCase("y") ? true : false;
	}
	
	public void progressbar()
	{
		try 
		{
		    for(int i =0; i < 29; i++) { System.out.print("."); Thread.sleep(25); }
		    System.out.println(" Done");
		    
		} catch (InterruptedException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} 
	}
	
	public void updatePacketSize()
	{
		allpacketlength++;
	}
}
