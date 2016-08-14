package Client;

import java.nio.channels.*;
import java.io.*;
import java.util.*;
import Function.*;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 24
 * 
 * Name : AutoBackup System
 * Description : It can backup encrypted files
 * 
 * */

public class Client_AutoBackup extends Thread implements PacketRule
{
	// File or Directory
	private File _checkProperty = null;
	private String _fileName = null;
	private long _fileSize = 0;
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	private String _absoluteDirectory = null;
	
	// stopFlag
	private boolean _stopFlag = false;
	
	// Sending Module
	private Client_Server_Connector _csc = null;
	
	// Constructors
	public Client_AutoBackup() 
	{
		try 
		{
			_csc = Client_Server_Connector.getInstance();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	// Methods
	public synchronized void run()
	{
		while(!_stopFlag)
		{
			if(!Client_FolderScan._directoryQueue.isEmpty())
			{
				_absoluteDirectory = Client_FolderScan._directoryQueue.remove();
				_checkProperty = new File(_absoluteDirectory);
				_fileName = _checkProperty.getName();
				_fileSize = _checkProperty.length();
				
				if(_checkProperty.isDirectory())
				{
					byte[] temp = new byte[1024];
					temp[0] = AUTOBACKUP;
					temp[1] = DIRECTORY;
					
					_csc.send.setPacket(temp).write();
					_csc.send.setPacket(_fileName.getBytes()).write();
				}
				else if(_checkProperty.isFile())
				{
					try 
					{
						boolean checkExtension = extensionTokenizer();
						
						if(checkExtension)
						{
							_raf = new RandomAccessFile(_absoluteDirectory, "rw");
							_fileChannel = _raf.getChannel();
							PacketProcessor p = new PacketProcessor(_fileChannel, false);

							byte[] temp = new byte[1024];
							temp[0] = AUTOBACKUP;
							temp[1] = FILE;
							temp[2] = (byte)String.valueOf(_fileSize).getBytes().length;
							temp[3] = (byte)_fileName.getBytes().length;
							Function.frontInsertByte(4, String.valueOf(_fileSize).getBytes(), temp);
							Function.frontInsertByte(4 + String.valueOf(_fileSize).getBytes().length, _fileName.getBytes(), temp);
							_csc.send.setPacket(temp).write();
							
							p.setAllocate(_fileSize);
							while(!p.isAllocatorEmpty())
							{
								_csc.send.setPacket(p.read().getByte()).write();
							}
							p.close();
						}
					} 
					catch (FileNotFoundException e) 
					{
						e.printStackTrace();
					} 
				}
			}
		}
	}
	
	private boolean extensionTokenizer()
	{
		StringTokenizer st = new StringTokenizer(_fileName, ".");
		String temp = null;
		
		while(st.hasMoreTokens())
		{
			temp = st.nextToken();
		}
		
		if(temp.equals("cnec"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void stopThread()
	{
		_stopFlag = true;
	}
}
