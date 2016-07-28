package Server;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;

import Function.EventTokenizer;

public class Server_AutoBackup extends Server_Funtion
{
	// Instance
	private String[] _token = null;
	private int _sendPacketSize = 0;
	
	// About File
	private String _checkProperty = null;
	private String _fileName = null;
	private long _fileSize = 0;
	
	// Another Class
	private EventTokenizer _et = null;
	
	// Constructors
	public Server_AutoBackup() 
	{
		_et = new EventTokenizer();
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
	
	private void setFileInformation()
	{
		_checkProperty = _token[1];
		_fileName = _token[2];
		if(_checkProperty.equals("FILE"))
		{
			_fileSize = Long.parseLong(_token[3]);
		}
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		_token = _et.getToken(packet);
		setFileInformation();
		
		if(_checkProperty.equals("DIRECTORY"))
		{
			_packetMaxCount = 1;
		}
		else if(_checkProperty.equals("FILE"))
		{
			_packetMaxCount = 1 + sendPacketSize(_fileSize);
		}
	}

	@Override
	public void running(Server_Client_Activity activity) 
	{
		
	}
	
}