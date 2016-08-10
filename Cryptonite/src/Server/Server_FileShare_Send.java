package Server;

import java.util.*;

import Function.PacketProcesser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 08 - 08
 * 
 * Name : FileShare
 * Description : It can share file
 * 
 * */

public class Server_FileShare_Send extends Server_Funtion
{
	// OTP Instance
	private String _OTP = null;
	private File _shareFolder = null;
	private String[] _fileList = null;
	private StringTokenizer _st = null;
	private boolean _temporaryFlag = false;
	private String _downloadFlag = null;
	
	// File Instance
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	private ArrayList<String> _fileCountList = null;
	private String _searchedFile = "DEFAULT";
	private String _fileName = null;
	private long _fileSize = 0;
	private byte[] _tempByte = null;
	
	// Methods
	private void changeByte(byte[] from)
	{
		_tempByte = new byte[1024];
		for(int i = 0; i < from.length; i++)
		{
			_tempByte[i] = from[i];
		}
	}
	
	private void OTP_Check()
	{
		try
		{
			_shareFolder = new File("C:\\Server\\Share");
			_fileList = _shareFolder.list();
			
			for(int i = 0; i < _fileList.length; i++)
			{
				_st = new StringTokenizer(_fileList[i], "★");
				if(_st.nextToken().equals(_OTP))
				{
					_searchedFile = _fileList[i];
					//_fileCountList.add(_fileList[i]);
					_temporaryFlag = true;
					break;
				}
			}
			if(_temporaryFlag)
			{
				_downloadFlag = "TRUE";
			}
			else
			{
				_downloadFlag = "FALSE";
			}
		}
		catch (NullPointerException e)
		{
			System.out.println("There is no file.");
		}
	}
	
	@Override
	public void Checker(byte[] packet, Server_Client_Activity activity) 
	{
		_activity = activity;
		_packetMaxCount = 1 + 1;
		_packetCutSize = 1;
		_activity.receive.setAllocate(30);
		//_fileCountList = new ArrayList<String>();
	}

	@Override
	public void running() 
	{
		_OTP = new String(_activity.receive.getByte()).trim();
		System.out.println("Receiving OTP : " + _OTP);
		OTP_Check();
		_activity.send.setPacket(_downloadFlag.getBytes()).write();
		
		if(_temporaryFlag)
		{
			/*changeByte(String.valueOf(_fileCountList.size()).getBytes());
			_activity.send.setPacket(_tempByte).write();*/
			
			/*for(int i = 0; i < _fileCountList.size(); i++)
			{*/
			try 
			{
				File sendingFile = new File("C:\\Server\\Share" + "\\" + _searchedFile);
				StringTokenizer st_temp = new StringTokenizer(_searchedFile, "★");
				int count = 0;
				while(st_temp.hasMoreTokens())
				{
					_fileName = st_temp.nextToken();
				}
				_fileSize = sendingFile.length();
				
				changeByte(_fileName.getBytes());
				_activity.send.setPacket(_tempByte, 1024).write();
				System.out.println("파일 이름(서버) : " + _fileName);
				
				changeByte(String.valueOf(_fileSize).getBytes());
				_activity.send.setPacket(_tempByte, 1024).write();
				System.out.println("파일 용량(서버) : " + _fileSize);
				
				_raf = new RandomAccessFile("C:\\Server\\Share" + "\\" + _searchedFile, "rw");
				_fileChannel = _raf.getChannel();
				PacketProcesser p = new PacketProcesser(_fileChannel, false);
				p.setAllocate(_fileSize);
				
				while(!p.isAllocatorEmpty())
				{
					_activity.send.setPacket(p.read().getByteBuf()).write();
				}
				p.close();
				
				//_fileCountList.remove(0);
				
				/*ByteBuffer buffer;
				while(_fileSize > 0)
				{
					buffer = ByteBuffer.allocateDirect(1024);
					buffer.clear();
					_fileChannel.read(buffer);
					_fileSize -= 1024;
					//buffer.flip();
					_activity.send.setPacket(buffer).write();
				}
				_fileChannel.close();*/
				_raf.close();
				sendingFile.delete();
			}
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			//}
			
			/*for(int i = 0; i < _fileCountList.size(); i++)
			{
				File sendingFile = new File("C:\\Server\\Share" + "\\" + _fileCountList.get(i));
				sendingFile.delete();
			}*/
		}
	}
}
