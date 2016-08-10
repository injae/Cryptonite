package Client;

import java.nio.*;
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
		_encryptedVector = new Vector<String>();
		_encryptedVector.add("C:\\Server\\test.mp3");

		try 
		{
			sleep(1);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		if(!_encryptedVector.isEmpty())
		{
			_checkProperty = new File(_encryptedVector.get(0));
			_fileName = _checkProperty.getName();
			_fileSize = _checkProperty.length();
			
			if(_checkProperty.isDirectory())
			{
				// 난 디렉토리다, 디렉토리명
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
					_raf = new RandomAccessFile(_encryptedVector.get(0),"rw");
					_fileChannel = _raf.getChannel();
					
					byte[] temp = new byte[1024];
					temp[0] = AUTOBACKUP;
					temp[1] = FILE;
					temp[2] = (byte)String.valueOf(_fileSize).getBytes().length;
					temp[3] = (byte)_fileName.getBytes().length;
					Function.frontInsertByte(4, String.valueOf(_fileSize).getBytes(), temp);
					Function.frontInsertByte(4 + String.valueOf(_fileSize).getBytes().length, _fileName.getBytes(), temp);
					_csc.send.setPacket(temp).write();
					
					ByteBuffer buffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
					while(_fileSize > 0)
					{						
						buffer.clear();
						_fileSize -= 1024;
						_fileChannel.read(buffer);
						_csc.send.setPacket(buffer).write();
					}
					_fileChannel.close();
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
			
			_encryptedVector.remove(0);
		}
	
	}
	
	public void stopThread()
	{
		_stopFlag = true;
	}
}
