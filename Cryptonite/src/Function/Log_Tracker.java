package Function;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log_Tracker
{
	private static Log_Tracker _log;
	
	private Log_Tracker()
	{
		
	}
	
	public void getInstance()
	{
		
	}
	
	public static void Print_cmd(String class_name ,String msg)
	{	
		Date now = new Date();
		String time = new SimpleDateFormat().format(now);
		System.out.println("[Time]: "+time+", [Class]: "+ class_name +", [Message]: "+msg+"");
	}
	
}
