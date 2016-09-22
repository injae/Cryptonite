package Function;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Logger
{
	private LinkedList<String> _log;
	private Date _now;
	private String _title;
	public Logger(String title)
	{
		_title = title;
		_log = new LinkedList<String>();
	    _now = new Date();
	}
	
	public void update(String log)
	{
		String time = new SimpleDateFormat().format(_now);
		_log.add("[Time]: " + time + "弛 " +log);
	}
	
	public int size()
	{
		return _log.size();
	}
	
	public String getLog(int index)
	{
		return _log.get(index);
	}
	
	public void printLogtoDos()
	{
		System.out.println("\n < Title: "+_title +" >");
		System.out.println("忙式式式式式式式式式式式式式式忖");
		for(int i =0; i < size(); i++)
		{
			System.out.println("弛" +getLog(i));
		}
		System.out.println("戌式式式式式式式式式式式式式式戎\n");
	}
	
	public void printLogTextFile(String path)
	{
		FileWriter fw;
		BufferedWriter bw;
		try 
		{
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			
			bw.write(" < Title: "+_title +" > "); bw.newLine();
			bw.write("-" + new SimpleDateFormat().format(_now) +"-"); bw.newLine();
			bw.write("忙式式式式式式式式式式式式式式忖"); bw.newLine();
			for(int i =0; i < size(); i++)
			{
				bw.write("弛" +getLog(i)); bw.newLine();
			}
			bw.write("戌式式式式式式式式式式式式式式戎"); bw.newLine();
			bw.close();
			fw.close();
		} 
		catch (IOException e) 
		{
			System.out.println("no path or no file name");
		}

		
	}
}
