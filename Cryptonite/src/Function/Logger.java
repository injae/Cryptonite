package Function;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Logger
{
	private LinkedList<String> _log;
	
	public Logger()
	{
		_log = new LinkedList<String>();
	}
	
	public void update(String log)
	{
		_log.add(log);
	}
	
	
	public static void Print_cmd(String class_name ,String msg)
	{	
		Date now = new Date();
		String time = new SimpleDateFormat().format(now);
		System.out.println("[Time]: "+time+", [Class]: "+ class_name +", [Message]: "+msg+"");
	}
	
}
