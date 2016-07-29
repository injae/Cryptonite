package Server;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import Function.PacketRule;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 29
 * 
 * Name : FileShare
 * Description : It can share file
 * 
 * */

public class Server_FileShare_Receive extends Server_Funtion implements PacketRule
{
	// Address
	private String _address = "C:\\Server\\Backup";
	
	// Instance
	private int _fileCount = 0;
	private String _fileName = null;
	private long _fileSize = 0;
	
	// FileChannel and RandomAccessFile
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	
	// Constructors
	public Server_FileShare_Receive() { }
	
	// Methods
	private int sendPacketSize(long fileSize)
	{
		int remainder = (int)(fileSize / FILE_BUFFER_SIZE);
		if((fileSize % FILE_BUFFER_SIZE) > 0)
		{
			remainder++;
		}
		
		return remainder;
	}
	
	private void setFileInformation(byte[] packet)
	{
		_fileCount = packet[1];
		
		byte[] sizeTemp = new byte[8];
		for(int i = 0; i < sizeTemp.length; i++)
		{
			sizeTemp[i] = packet[i + 2];
		}
		_fileSize = Long.parseLong(new String(sizeTemp).trim());
		
		int max = 10;
		while(packet[max] != 0)
		{
			max++;
		}
		
		byte[] nameTemp = new byte[max - 10];
		for(int i = 0; i < nameTemp.length; i++)
		{
			nameTemp[i] = packet[i + 10];
		}
		_fileName = new String(nameTemp).trim();
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		setFileInformation(packet);
		_packetMaxCount = 1 + sendPacketSize(_fileSize);
		
		try 
		{
			_raf = new RandomAccessFile(_address + "\\" + _fileName, "rw");
			_fileChannel = _raf.getChannel();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void running(Server_Client_Activity activity)
	{
		
	}
}
