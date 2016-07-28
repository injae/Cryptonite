package Function;

public class Function 
{
	public static void frontInsertByte(int start, byte[] from, byte[] to)
	{
		for(int i = 0 ; i < from.length; i++)
		{
			to[i + start] = from[i];
		}
	}
	
	public static void backInsertByte(int start, byte[] from, byte[] to)
	{
		for(int i = 0 ; i < to.length; i++)
		{
			to[i] = from[i+2];
		}
	}
}
