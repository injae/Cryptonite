package Function;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Logger
{
	private LinkedList<String> _log;
	private Date _now;
	public Logger()
	{
		_log = new LinkedList<String>();
	    _now = new Date();
	}
	
	public void update(String log)
	{
		String time = new SimpleDateFormat().format(_now);
		_log.add("[Time]: " + time + " | " +log);
	}
	
	public int size()
	{
		return _log.size();
	}
	
	public String getLog(int index)
	{
		return _log.get(index);
	}
}
