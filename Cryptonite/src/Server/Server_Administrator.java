package Server;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.plaf.synth.SynthSpinnerUI;

import Client.Client_FileRecovery.RecoveryButton;
import Function.Logger;
import Function.Timer;

public class Server_Administrator extends Thread
{
	private Logger userInfo;
	private Logger errorInfo;
	private Logger inoutInfo;
	
	private String id;
	private long allpacketlength = 0;
	private Timer timer;
	private long accumulateUser = 0;
	
	public Server_DosManager sdm;
	
	private static Server_Administrator instance;
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
	    
		sdm = new Server_DosManager(id);
		
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
	
	public void print(String msg)
	{
		try 
		{
			sdm.send(msg);
			System.out.println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String input()
	{
		String result = " ";
		try 
		{
			result = sdm.receive();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return result;
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
			try 
			{
				StringTokenizer command = new StringTokenizer(sdm.receive(), " ");
							
				switch(command.nextToken())
				{
				case "help":
					if(command.hasMoreTokens())
					{
						String query = command.nextToken();					
						switch(query)
						{
						case "-log":
							sdm.send("5");
							print("* -u   <>      	  : User log");
							print("* -e   <>     	  : Error log");
							print("* -p   <>  		  : input output log");
							print("* <>   -w  <path> : make log file -> path");
							print("* <>   -b         : print log dos ps. this is default print");
							break;
							
						case "-size":
							sdm.send("2");
							print("* -p         : input packet size");
							print("* -f         : server file size");
							break;
						}
					}
					else
					{
						sdm.send("6");
						print("  <command> :  <explain>");
						print("*   size    : size of all packet   More info command > help -size ");
						print("*   user    : how many user count");
						print("*   time    : server running time");
						print("*   stop    : server stop");
						print("*   log     : print log,           More info command > help -log  ");
					}
					break;
					
				case "size":
					while(command.hasMoreTokens())
					{
						String query = command.nextToken();
						switch(query)
						{
						case "-p":
							sdm.send("1");
							print("All Packet Size: " + convertByteUnit(allpacketlength)); break;
						case "-f":		
							sdm.send("1");
							print("All file Size: " + convertByteUnit(folderSize("Server_Folder"))); break;						
						}		
					}
					break;
					
				case "user":
					sdm.send("1");
					print("How many user: " +Server_Client_Manager.getInstance().HowManyClient());	 break;
					
				case "time":
					long hour = timer.runningTime() / (3600 * 1000);
					
					long min  = timer.runningTime() / (60   * 1000);
					if(min >= 60){ min %= 60;}
					
					long sec = timer.runningTime()  / (		  1000);
					if(sec >= 60){ sec %= 60;}
					
					sdm.send("1");
					print("Running Time: "+hour+"h." +min + "m." + sec +"s"); break;
					
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
					if(YorN()){ System.exit(1); } break;
					
				default:
					sdm.send("0");
				}
			} catch (IOException e) {
				sdm.reConnect();
			}
		}
	
	}
	
	private long folderSize(String path)
	{
		long fullsize = 0;
		
		File dir = new File(path);
		File[] files = dir.listFiles();
		
		for(int i =0; i < files.length; i++)
		{
			if(files[i].isDirectory()) { fullsize += folderSize(files[i].getPath()); }
			else { fullsize += files[i].length();}
		}
		
		return fullsize;
	}
	
	private String convertByteUnit(long bytesize)
	{
		long remain;
		long gb = bytesize / (1024 * 1024 * 1024); remain = bytesize % (1024 * 1024 * 1024);
		long mb = remain / (1024 * 1024);	remain %= (1024 * 1024);
		long kb = remain / 1024;	remain %= 1024;
		long  b = remain; 		
		
		return gb+"GB, "+ mb +"MB ,"+ kb+"KB, "+b+"Byte";
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
	public void addAcus() { accumulateUser++; }
	
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
			e.printStackTrace();
		} 
	}
	
	public void packetSizeUpdate(int size)
	{
		allpacketlength+= size;
	}
}
