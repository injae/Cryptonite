package Client;

import Function.PacketRule;
import java.net.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.nio.*;
import java.util.*;
import java.io.*;

public class Client_FileShare_Send implements PacketRule
{
	// Instance
	private String _OTP = null;
	
	// About Files
	private String[] _fileNameArray = null;
	private int[] _fileNameSize = null;
	
	private String[] _filePathArray = null;
	private int[] _filePathSize = null;
	
	private File _tempFile = null;
	private long[] _fileSizeArray = null;
	
	// Another Class Instance
	private Client_Server_Connector _csc = null;
	private Client_FileSelector _cfs = null;
	
	// Constructors
	public Client_FileShare_Send()
	{
		try 
		{
			_csc = Client_Server_Connector.getInstance(4444);
			_cfs = new Client_FileSelector();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	// Methods
	public void start()	// when you click the button which turn on 1:1 file share system.
	{
		_cfs.fileFinderON();
		while(!_cfs.getSelectionFinish())
		{
			try 
			{
				Thread.sleep(5);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		_fileNameArray = _cfs.getFileNames();
		_fileNameSize = new int[_fileNameArray.length];
		for(int i = 0; i < _fileNameSize.length; i++)
		{
			_fileNameSize[i] = _fileNameArray[i].length();
		}
		
		_filePathArray = _cfs.getFilePaths();
		_filePathSize = new int[_filePathArray.length];
		for(int i = 0; i < _filePathSize.length; i++)
		{
			_filePathSize[i] = _filePathArray[i].length();
		}
		
		_fileSizeArray = new long[_filePathArray.length];
		for(int i = 0; i < _filePathArray.length; i++)
		{
			_tempFile = new File(_filePathArray[i]);
			_fileSizeArray[i] = _tempFile.length();
		}
	}
	
	public void sendFile()	// when you click send button
	{
		_csc.configurePacket("FILE_SHARE_SEND");
		byte[] packet = new byte[100];
		packet[0] = FILE_SHARE_SEND;
		packet[1] = (byte)_fileNameArray.length;
		for(int i = 0; i < _fileNameArray.length; i++)
		{
			packet[i + 2] = (byte)_fileNameSize[i];
		}
		for(int i = 0; i < _filePathArray.length; i++)
		{
			packet[i + 2 + _filePathArray.length] = (byte)_filePathSize[i];
		}
	}
	
	private void receiveOTP()
	{
		
	}
}
