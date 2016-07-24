package Client;

import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.io.*;
import java.util.*;
import Function.NIO_FileManager;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 24
 * 
 * Name : AutoBackup System
 * Description : It can backup encrypted files
 * 
 * */

public class Client_AutoBackup extends Thread
{
	// SocketChannel
	private SocketChannel _socketChannel = null;
	private int _port = 4444;
	
	// Buffer
	private ByteBuffer _buffer = null;
	
	// File or Directory
	private File _checkProperty = null;
	private FileChannel _fileChannel = null;
	private String _fileName = null;
	private long _totalSize;
	
	// temporary encryptedVector
	private Vector<String> _encryptedVector = null;
	
	// Constructors
	public Client_AutoBackup()
	{
		try 
		{
			_socketChannel = SocketChannel.open();
			_socketChannel.configureBlocking(true);
			System.out.println("[ Connecting requesting ]");
			_socketChannel.connect(new InetSocketAddress("localhost", _port));
			System.out.println("[ Connecting Success ]");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	// Methods
	public synchronized void run()
	{
		try 
		{	
			while(!_socketChannel.finishConnect())
			{
				sleep(1);
				if(!_encryptedVector.isEmpty())
				{
					_checkProperty = new File(_encryptedVector.get(0));
					_fileName = _checkProperty.getName();
					
					Charset charSet = Charset.forName("UTF-8");
					if(_checkProperty.isDirectory())
					{
						_buffer = charSet.encode("Directory");
						_socketChannel.write(_buffer);
					}
					else if(_checkProperty.isFile())
					{
						_totalSize = _checkProperty.length();
						Path _path = Paths.get(_encryptedVector.get(0));
						_fileChannel = FileChannel.open(_path, StandardOpenOption.READ);
						
						_buffer = charSet.encode("File");
						_socketChannel.write(_buffer);
						_buffer.clear();
						
						_buffer = charSet.encode(_fileName);
						_socketChannel.write(_buffer);
						_buffer.clear();
						System.out.println("fileName Sending Complete !!");
						
						_buffer = charSet.encode(String.valueOf(_totalSize));
						_socketChannel.write(_buffer);
						_buffer.clear();
						System.out.println("fileSize Sending Complete !!");
						
						System.out.println("Now File [ " + _fileName + " ] Sending ......");
						while(_totalSize > 0)
						{
							if(_totalSize < 1024)
							{
								_buffer = ByteBuffer.allocateDirect((int)_totalSize);
							}
							else
							{
								_buffer = ByteBuffer.allocateDirect(1024);
							}
							_totalSize -= 1024;
							_fileChannel.read(_buffer);
							_buffer.flip();
							_socketChannel.write(_buffer);
							_buffer.clear();
						}
						System.out.println("[ " + _fileName + " ] Sending Complete !!");
					}
					
					_encryptedVector.remove(0);
				}
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void stopThread()
	{
		try 
		{
			_socketChannel.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
