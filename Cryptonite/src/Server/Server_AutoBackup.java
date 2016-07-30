package Server;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;
import Function.*;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 29
 * 
 * Name : AutoBackup System
 * Description : It can backup encrypted files
 * 
 * */

public class Server_AutoBackup extends Server_Funtion implements PacketRule
{
	// protectedFolder
	private String _address = "C:\\테스트";
	private File _protectedFolder = new File(_address);
	
	// Instance
	private String[] _token = null;
	private int _count = 1;
	
	// About File
	private String _checkProperty = null;
	private String _fileName = null;
	private long _fileSize = 0;
	
	private RandomAccessFile _raf;
	private FileChannel _fileChannel;
	
	// Another Class
	private EventTokenizer _et = null;
	
	// Constructors
	public Server_AutoBackup() { }
	
	// Methods
	private int sendPacketSize(long fileSize)
	{
		int remainder = (int)(fileSize / FILE_BUFFER_SIZE);
		if((fileSize % FILE_BUFFER_SIZE) > 0)
		{
			remainder++;
		}
		
		return remainder;
	}
	
	private void setFileInformation(byte[] packet)
	{
		_checkProperty = "FILE";
		
		int end = 0;
		
		byte[] sizeTemp = new byte[packet[2]];
		for(int i = 0; i < sizeTemp.length; i++)
		{
			sizeTemp[i] = packet[i + 4];
			end = i+4;
		}
		_fileSize = Long.parseLong(new String(sizeTemp).trim());
		
		int max = end;
		while(packet[max] != 0)
		{
			max++;
		}
		
		byte[] nameTemp = new byte[packet[3]];
		for(int i = 0; i < nameTemp.length; i++)
		{
			nameTemp[i] = packet[i + end + 1];
		}
		_fileName = new String(nameTemp).trim();
		System.out.println("파일 이름 : " + _fileName);
		System.out.println("파일 용량 : " + _fileSize + " (Byte)");
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		if(packet[1] == DIRECTORY)
		{
			_checkProperty = "DIRECTORY";
			_packetMaxCount = 2;
		}
		else if(packet[1] == FILE)
		{
			setFileInformation(packet);
			_packetMaxCount = 1 + sendPacketSize(_fileSize);
			
			try {
				_raf = new RandomAccessFile(_address + "\\" + _fileName, "rw");
				_fileChannel = _raf.getChannel();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		System.out.println("packetMaxCount : " + _packetMaxCount);
	}

	@Override
	public void running(Server_Client_Activity activity) 
	{
		System.out.println("NOW AUTOBACKUP RUNNING");
		if(_checkProperty.equals("DIRECTORY"))
		{
			_fileName = new String(activity._receiveQueue.remove()).trim();
			File newFolder = new File(_address + "\\" + _fileName);
			newFolder.mkdir();
		}
		else if(_checkProperty.equals("FILE"))
		{
			try 
			{
				ByteBuffer buffer;
				buffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
				while(activity.IsReadable())
				{
					_count++;
					buffer.clear();
					buffer.put(activity._receiveQueue.remove());
					
					_fileSize -= FILE_BUFFER_SIZE;
					buffer.flip();
					while(!_fileChannel.isOpen())
					{
						Thread.sleep(1);
					}
					_fileChannel.write(buffer);
					
					if(_fileSize <= 0)
					{
						break;
					}
				}
				
				if(_count == _packetMaxCount)
				{
					System.out.println("끝");
					_fileChannel.close();
					_count = 1;
				}
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
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
	}
}