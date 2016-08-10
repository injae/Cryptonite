package Client;

import Function.*;
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
	private FileChannel _fileChannel = null;
	private RandomAccessFile _raf = null;
	
	private String[] _fileNameArray = null;
	private int[] _fileNameSize = null;
	
	private String[] _filePathArray = null;
	
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
			_csc = Client_Server_Connector.getInstance();
			_cfs = new Client_FileSelector();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	// Methods
	public void click()	// when you click the button which turn on 1:1 file share system.
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
		
		_fileSizeArray = new long[_filePathArray.length];
		for(int i = 0; i < _filePathArray.length; i++)
		{
			_tempFile = new File(_filePathArray[i]);
			_fileSizeArray[i] = _tempFile.length();
		}
		
		sendFile();	// Temporary
	}
	
	public void sendFile()	// when you click send button
	{
		byte[] garbage = new byte[1024];

		byte[] OTP_Packet = new byte[100];
		OTP_Packet[0] = MAKE_OTP;
		_csc.send.setPacket(OTP_Packet).write();
		_csc.send.setPacket(garbage).write();			
		
		byte[] OTP_Byte = _csc.receive.read().getByte();
		_OTP = new String(OTP_Byte).trim();

		changeFilesName();
		
		for(int i = 0; i < _fileNameArray.length; i++)
		{
			try 
			{
				byte[] packet = new byte[1024];
				packet[0] = FILE_SHARE_RECEIVE;
				packet[1] = (byte)_fileNameArray.length;
				packet[2] = (byte)String.valueOf(_fileSizeArray[i]).getBytes().length;
				packet[3] = (byte)_fileNameArray[i].getBytes().length;
				Function.frontInsertByte(4, String.valueOf(_fileSizeArray[i]).getBytes(), packet);
				Function.frontInsertByte(4 + String.valueOf(_fileSizeArray[i]).getBytes().length, _fileNameArray[i].getBytes(), packet);
				_csc.send.setPacket(packet).write();	// 1
				//_csc.send.setAllocate(_fileSizeArray[i]);
				
				_raf = new RandomAccessFile(_filePathArray[i], "rw");
				_fileChannel = _raf.getChannel();
				PacketProcesser p = new PacketProcesser(_fileChannel, false);
				p.setAllocate(_fileSizeArray[i]);
				
				while(!p.isAllocatorEmpty())
				{
					_csc.send.setPacket(p.read().getByteBuf()).write();
				}
				p.close();
				System.out.println(_fileNameArray[i] + " 파일이 전송이 완료되었습니다.");			
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				System.exit(1);
				e.printStackTrace();
			}
		}
		System.out.println("전부 보냈어요");
	}
	
	private void changeFilesName()
	{
		String temp = "";
		for(int i = 0; i < _fileNameArray.length; i++)
		{
			temp = _fileNameArray[i];
			_fileNameArray[i] = _OTP + "★" + temp;
		}
	}
}
