package Server;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;

public class Server_AutoBackup extends Server_Funtion
{
	// Instance
	private File _checkProperty = null;
	private String _temp = null;
	private String _fileName = null;
	private long _fileSize = 0;
	
	// Constructors
	public Server_AutoBackup() 
	{
		
	}
	
	// Methods
	private int sendPacketSize(long fileSize)
	{
		int remainder = (int)(fileSize / 1024);
		if((fileSize % 1024) > 0)
		{
			remainder++;
		}
		
		return remainder;
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		
		//_packetMaxCount = ;
	}

	@Override
	public void running(Server_Client_Activity activity) 
	{
		
	}
	
}