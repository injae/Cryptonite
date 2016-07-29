package Client;

import Function.PacketRule;
import java.net.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.nio.*;
import java.util.*;
import java.io.*;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 29
 * 
 * Name : FileShare
 * Description : It can share file
 * 
 * */

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
	private byte[][] _fileSizeByte = null;
	
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
		_fileSizeByte = new byte[_fileSizeArray.length][];
		for(int i = 0; i < _fileSizeArray.length; i++)
		{
			_fileSizeByte[i] = new byte[8];
		}
		
		for(int i = 0; i < _filePathArray.length; i++)
		{
			_tempFile = new File(_filePathArray[i]);
			_fileSizeArray[i] = _tempFile.length();
			_fileSizeByte[i] = String.valueOf(_tempFile.length()).getBytes();
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
		_csc.setPacket("FILE_SHARE_SEND", packet);
		
		byte[] packet2 = new byte[8 * _fileSizeByte.length];
		for(int i = 0; i < _fileSizeByte.length; i++)
		{
			for(int j = i * 8; j < 8 * (i + 1); j++)
			{
				int k = 0;
				packet2[j] = _fileSizeByte[i][k];
				k++;
			}
		}
		_csc.setPacket("FILE_SHARE_SEND", packet2);
		
		// write file sending code
		_csc.send("FILE_SHARE_SEND");
	}
	
	private void receiveOTP()
	{
		
	}
}
