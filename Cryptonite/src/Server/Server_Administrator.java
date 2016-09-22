package Server;

import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.plaf.synth.SynthSpinnerUI;

import Function.Logger;
import Function.Timer;

public class Server_Administrator extends Thread
{
	private Timer timer;
	private static Server_Administrator instance;
	private int allpacketlength = 0;
	
	private Logger userInfo;
	private Logger errorInfo;
	private Logger inoutInfo;
	
	private String id;
	
	private Server_Administrator() 
	{
		timer = new Timer();
		userInfo = new Logger("User Log");
		errorInfo = new Logger("Error Log");
		inoutInfo = new Logger("Input Output Log");
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("<DataBase Login>");
		System.out.print("id : ");       id = input.nextLine();
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
			System.out.print("["+ id +"]"+" > ");
			StringTokenizer command = new StringTokenizer(input.nextLine(), " ");
			
			switch(command.nextToken())
			{
			case "help":
				if(command.hasMoreTokens())
				{
					String query = command.nextToken();
					
					switch(query)
					{
					case "-log":
						System.out.println("* -u         : User log");
						System.out.println("* -e         : Error log");
						System.out.println("* -p         : input output log");
						System.out.println("* -w  <path> : make log file -> path");
						System.out.println("* -b         : print log dos ps. this is default print");
						break;
					}
				}
				else
				{
					System.out.println("  <command> :  <explain>");
					System.out.println("*   size    : size of all packet");
					System.out.println("*   user    : how many user count");
					System.out.println("*   time    : server running time");
					System.out.println("*   stop    : server stop");
					System.out.println("*   log     : print log, more info command > help -log ");
				}
				break;
				
			case "size":
				System.out.println("All Packet Size: "+allpacketlength +" byte");	 break;
				
			case "user":
				System.out.println("How many user: " +Server_Client_Manager.getInstance().HowManyClient());	 break;
				
			case "time":
				long hour = timer.runningTime() / (3600 * 1000);
				
				long min  = timer.runningTime() / (60   * 1000);
				if(min >= 60){ min %= 60;}
				
				long sec = timer.runningTime()  / (		  1000);
				if(sec >= 60){ sec %= 60;}
				
				System.out.println("Running Time: "+hour+"h." +min + "m." + sec +"s"); break;
				
			case "log":
				Logger log = null;
				while(command.hasMoreTokens())
				{
					String query = command.nextToken();
					switch(query)
					{
					case "-u":
					case "-p":
					case "-e":
						if(log != null) 			 { log.printLogtoDos(); }
						
						log = selectLog(query); 
						
						if(!command.hasMoreTokens()) { log.printLogtoDos(); }
						break;
					case "-w":
						if(log == null || !command.hasMoreTokens()) break;						
						query = command.nextToken();
						log.printLogTextFile(query);
						break;
						
					case "-d":
						if(log == null) break;		
						log.printLogtoDos();
					}
				}
				break;
				
			case "stop":
				if(YorN()){ System.exit(1); }
			}
		}
	}
	
	private Logger selectLog(String command)
	{
		switch(command)
		{
		case "-u":
			return userInfo;
		case "-e":
			return errorInfo;
		case "-p":
			return inoutInfo;
		default:
			return new Logger("");
		}
	}
	
	public void inoutUpdate(String str) { inoutInfo.update(str); }
	
	public void errorUpdate(String str) { errorInfo.update(str); }
	
	public void userUpdate(String str)  { userInfo.update(str); }
	
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
	
	public void packetSizeUpdate(int size)
	{
		allpacketlength+= size;
	}
}
