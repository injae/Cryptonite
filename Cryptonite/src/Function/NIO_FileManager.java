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
 * 4. CREATE_NEW = StandardOpenOption.CREATE_NEW
 * 
 * */

public class NIO_FileManager 
{
	// Path
	private Path _path = null;
	
	// ByteBuffer
	private ByteBuffer _buffer = null;
	
	// FileChannels
	private FileChannel _fileChannel_address = null;
	
	// FileQueue
	private Vector<ByteBuffer> _fileQueue = null;
	
	// Constructors
	public NIO_FileManager(Path address, String mode)
	{
		_buffer = ByteBuffer.allocateDirect(1024);
		_path = address;
	}
	
	// Methods
	public void fileManage(String mode)
	{
		if(mode.equals("READ"))
		{
			try 
			{
				_fileChannel_address = FileChannel.open(_path, StandardOpenOption.READ);
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
				_fileChannel_address = FileChannel.open(_path, StandardOpenOption.WRITE);
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
				_fileChannel_address = FileChannel.open(_path, StandardOpenOption.CREATE);
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
	
	public Vector<ByteBuffer> getFileQueue()
	{
		return _fileQueue;
	}
}
