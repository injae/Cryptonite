package Client;

import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.io.*;
import java.util.*;
import Function.NIO_FileManager;
import Function.PacketRule;

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
	private SocketChannel _socketChannel = null;
	private int _port = 4444;
	
	// Buffer
	private ByteBuffer _buffer = null;
	
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
				
				Charset charSet = Charset.forName("UTF-8");
				_csc.configurePacket("AUTOBACKUP");
				
				_buffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
				_buffer.clear();
				
				if(_checkProperty.isDirectory())
				{
					// 난 디렉토리다, 디렉토리명
					byte[] temp1 = "AUTOBACKUP\n".getBytes();
					byte[] temp2 = "DIRECTORY\n".getBytes();
					byte[] temp3 = _fileName.getBytes();
					
					_buffer.put(temp1);
					_buffer.put(temp2);
					_buffer.put(temp3);

					_buffer.flip();
					_csc.setPacket("AUTOBACKUP", _buffer);
					_csc.send("AUTOBACKUP");
				}
				else if(_checkProperty.isFile())
				{
					try 
					{
						ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
						buf.putLong(_fileSize);
						
						_raf = new RandomAccessFile(_encryptedVector.get(0),"rw");
						_fileChannel = _raf.getChannel();
						
						byte[] temp1 = "AUTOBACKUP\n".getBytes();
						byte[] temp2 = "FILE\n".getBytes();
						byte[] temp3 = (_fileName + "\n").getBytes();
						byte[] temp4 = buf.array();
						
						_buffer.put(temp1);
						_buffer.put(temp2);
						_buffer.put(temp3);
						_buffer.put(temp4);
						
						_buffer.flip();
						_csc.setPacket("AUTOBACKUP", _buffer);
						_csc.send("AUTOBACKUP");
						
						while(_fileSize > 0)
						{
							if(_fileSize < FILE_BUFFER_SIZE)
							{
								_buffer = ByteBuffer.allocateDirect((int)_fileSize);
							}
							else
							{
								_buffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
							}
							_buffer.clear();
							_fileSize -= 1024;
							_fileChannel.read(_buffer);
							_csc.setPacket("AUTOBACKUP", _buffer);
							_buffer.flip();
							_csc.send("AUTOBACKUP");
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
	}
	
	public void stopThread()
	{
		_stopFlag = true;
	}
}
