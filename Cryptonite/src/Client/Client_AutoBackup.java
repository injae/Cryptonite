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
	
	// stopFlag
	private boolean _stopFlag = false;
	
	// temporary encryptedVector
	private Vector<String> _encryptedVector = null;
	
	// Sending Module
	private Client_Server_Connector _csc = null;
	
	// Constructors
	public Client_AutoBackup() 
	{

		_csc = Client_Server_Connector.getInstance();
	
	}
	
	// Methods
	public synchronized void run()
	{
		_encryptedVector = new Vector<String>();
		_encryptedVector.add("C:\\Server\\test.mp3");

		if(!_encryptedVector.isEmpty())
		{
			_checkProperty = new File(_encryptedVector.get(0));
			_fileName = _checkProperty.getName();
			_fileSize = _checkProperty.length();
			
			if(_checkProperty.isDirectory())
			{
				byte[] temp = new byte[1024];
				temp[0] = AUTOBACKUP;
				temp[1] = DIRECTORY;
				
				try 
				{
					_csc.send.setPacket(temp).write();
					_csc.send.setPacket(_fileName.getBytes()).write();
				} 
				catch (IOException e) 
				{
					// TODO 자동 생성된 catch 블록
					e.printStackTrace();
				}
			}
			else if(_checkProperty.isFile())
			{
				try 
				{
					_raf = new RandomAccessFile(_encryptedVector.get(0),"rw");
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
				catch (IOException e) 
				{
					e.printStackTrace();
				} 
			}
			
			_encryptedVector.remove(0);
		}
	
	}
	
	public void stopThread()
	{
		_stopFlag = true;
	}
}
