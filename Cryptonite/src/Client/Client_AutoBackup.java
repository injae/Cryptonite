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
				if(_checkProperty.isDirectory())
				{
					// 오토백업이다, 난 디렉토리다, 디렉토리명
					// 
					_buffer = ByteBuffer.allocateDirect(100);
					_buffer.clear();
					
					byte[] event = new byte[100];
					event[0] = AUTOBACKUP;
					_csc.setPacket("AUTOBACKUP", _buffer);
					_buffer.clear();
					
					
				}
				else if(_checkProperty.isFile())
				{
					// 오토백업이다, 난 파일이다, 파일이름, 파일사이즈, 파일
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
