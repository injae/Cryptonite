package Server;

import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

import Function.Timer;

public class Server_Administrator extends Thread
{
	private Timer timer;
	private static Server_Administrator instance;
	private int allpacketlength = 0;
	
	private Server_Administrator() 
	{
		timer = new Timer();
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
	
	public void progressbar()
	{
		try 
		{
		    for(int i =0; i < 20; i++) { System.out.print("."); Thread.sleep(25); }
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
