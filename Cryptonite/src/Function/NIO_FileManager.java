package Function;

import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.net.*;

/*
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
		int _byteCount;
		_fileQueue = new Vector<ByteBuffer>();
		
		try {
			_buffer.clear();
			while((_byteCount = _fileChannel_address.read(_buffer)) != -1)
			{
				_buffer.flip();
				_fileQueue.add(_buffer);
				_buffer.clear();
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
