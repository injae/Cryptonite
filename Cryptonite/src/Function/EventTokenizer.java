package Function;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.net.*;

public class EventTokenizer 
{
	// StringTokenizer and Temp
	private StringTokenizer st = null;
	private String temp = null;
	private String[] token = null;
	
	// Constructors
	public EventTokenizer() { }
	
	// Methods
	public String[] getToken(byte[] buffer)
	{
		temp = new String(buffer).trim();
		
		st = new StringTokenizer(temp,"\n");
		token = new String[st.countTokens()];
		
		int i = 0;
		while(st.hasMoreTokens())
		{
			token[i] = st.nextToken();
		}
		
		return token;
	}
}
