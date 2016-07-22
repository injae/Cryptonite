package Function;

import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.net.*;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 22
 * 
 * Name : NIO - File Manager
 * Description : It will make easy to use nio file I/O
 * 
 * FileManager mode list
 * 
 * 1. READ = StandardOpenOption.READ
 * 2. WRITE = StandardOpenOption.WRITE
 * 3. CREATE = StandardOpenOption.CREATE
 * 
 * How to use??
 * 
 * NIO_FileManager test = new NIO_FileManager();
 * test.fileManage("c:\\test\\hello.txt", "READ");
 * test.fileManage("c:\\test\\hello2.txt", "WRITE");
 * 
 * */

public class NIO_FileManager 
{
	// ByteBuffer
	private ByteBuffer _buffer = null;
	
	// FileChannels
	private FileChannel _fileChannel_address = null;
	
	// FileQueue
	private Vector<ByteBuffer> _fileQueue = null;
	
	// Constructor
	public NIO_FileManager()
	{
		_buffer = ByteBuffer.allocateDirect(1024);
	}
	
	// Methods
	public void fileManage(Path path , String mode)
	{
		if(mode.equals("READ"))
		{
			try 
			{
				_fileChannel_address = FileChannel.open(path, StandardOpenOption.READ);
				fileRead();
			} 
		catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else if(mode.equals("WRITE"))
		{
			try 
			{
				_fileChannel_address = FileChannel.open(path, StandardOpenOption.WRITE);
				fileWrite();
			} 
		catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else if(mode.equals("CREATE"))
		{
			try 
			{
				_fileChannel_address = FileChannel.open(path, StandardOpenOption.CREATE);
			} 
		catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private void fileRead()
	{
		try 
		{
			long _totalSize = _fileChannel_address.size();
			_fileQueue = new Vector<ByteBuffer>();
			
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
				_fileChannel_address.read(_buffer);
				_buffer.flip();
				_buffer.clear();
				_fileQueue.add(_buffer);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void fileWrite()
	{
		while(_fileQueue.isEmpty() == false)
		{
			try 
			{
				_fileChannel_address.write(_fileQueue.get(0));
				_fileQueue.remove(0);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}