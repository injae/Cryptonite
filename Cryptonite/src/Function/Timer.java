package Function;

public class Timer 
{
	private long startTime;
	private long stopTime;
	public Timer() 
	{
		startTime = System.currentTimeMillis();
	}
	
	public void start()
	{
		startTime = System.currentTimeMillis(); 
	}
	
	public void stop()
	{
		stopTime = System.currentTimeMillis();
	}
	
	public boolean alarm(long time)
	{
		stop();
		
		if((stopTime - startTime) >= time) 
		{
			start();
			return true;
		}
		else
		{
			return false;
		}
	}
}
