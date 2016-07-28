package Client;

import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
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
	// SocketChannel
	private int _port = 4444;
	
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
			_csc = Client_Server_Connector.getInstance(4444);
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
		_encryptedVector.add("C:\\Users\\Youn\\Desktop\\이폴더가 생겨야돼");
		while(!_stopFlag)
		{
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
				
				_csc.configurePacket("AUTOBACKUP");
				
				if(_checkProperty.isDirectory())
				{
					// 난 디렉토리다, 디렉토리명
					byte[] temp = new byte[2];
					temp[0] = AUTOBACKUP;
					temp[1] = DIRECTORY;
					
					_csc.setPacket("AUTOBACKUP", temp);
					_csc.setPacket("AUTOBACKUP", _fileName.getBytes());
					
					_csc.send("AUTOBACKUP");
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
						Function.frontInsertByte(2, String.valueOf(_fileSize).getBytes(), temp);
						Function.frontInsertByte(10, _fileName.getBytes(), temp);
						_csc.setPacket("AUTOBACKUP", temp);
						
						ByteBuffer buffer = null;
						buffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
						while(_fileSize > 0)
						{
							if(_fileSize < FILE_BUFFER_SIZE)
							{
								buffer = ByteBuffer.allocateDirect((int)_fileSize);
							}
							/*else
							{
								buffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
							}*/
							buffer.clear();
							_fileSize -= 1024;
							_fileChannel.read(buffer);
							buffer.flip();
							_csc.setPacket("AUTOBACKUP", buffer);
						}
						
						_csc.send("AUTOBACKUP");
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
	}
	
	public void stopThread()
	{
		_stopFlag = true;
	}
}
