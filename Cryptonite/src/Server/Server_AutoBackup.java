package Server;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;
import Function.*;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 29
 * 
 * Name : AutoBackup System
 * Description : It can backup encrypted files
 * 
 * */

public class Server_AutoBackup extends Server_Funtion implements PacketRule
{
	// protectedFolder
	private String _address = "C:\\Å×½ºÆ®";
	private File _protectedFolder = new File(_address);
	
	// Instance
	private String[] _token = null;
	private int _count = 0;
	
	// About File
	private String _checkProperty = null;
	private String _fileName = null;
	private long _fileSize = 0;
	
	// Another Class
	private EventTokenizer _et = null;
	
	// Constructors
	public Server_AutoBackup() 
	{
		
	}
	
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
		_checkProperty = "FILE";
		
		byte[] sizeTemp = new byte[8];
		for(int i = 0 ; i < sizeTemp.length; i++)
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
		if(packet[1] == DIRECTORY)
		{
			_checkProperty = "DIRECTORY";
			_packetMaxCount = 2;
		}
		else if(packet[1] == FILE)
		{
			setFileInformation(packet);
			_packetMaxCount = 1 + sendPacketSize(_fileSize);
		}
		System.out.println("packetMaxCount : " + _packetMaxCount);
	}

	@Override
	public void running(Server_Client_Activity activity) 
	{
		System.out.println("NOW AUTOBACKUP RUNNING");
		if(_checkProperty.equals("DIRECTORY"))
		{
			_fileName = new String(activity._receiveQueue.remove()).trim();
			File newFolder = new File(_address + "\\" + _fileName);
			newFolder.mkdir();
		}
		else if(_checkProperty.equals("FILE"))
		{
			try 
			{
				RandomAccessFile raf = new RandomAccessFile(_address + "\\" + _fileName, "rw");
				FileChannel fileChannel = raf.getChannel();
				
				ByteBuffer buffer;
				while(activity.IsReadable())
				{
					_count++;
					if(_fileSize < FILE_BUFFER_SIZE)
					{
						buffer = ByteBuffer.allocateDirect((int)_fileSize);
					}
					else
					{
						buffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
					}
					buffer.put(activity._receiveQueue.remove());
					_fileSize -= FILE_BUFFER_SIZE;
					buffer.flip();
					fileChannel.write(buffer);
				}
				System.out.println("count : " + _count);
				if(_count == _packetMaxCount)
				{
					fileChannel.close();
				}
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}