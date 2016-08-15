package Server;

import java.util.*;

import Function.PacketProcessor;

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
	private String _searchedFile = "DEFAULT";
	private String _fileName = null;
	private long _fileSize = 0;
	
	private void OTP_Check()
	{
		try
		{
			_shareFolder = new File("Server_Folder\\Share");
			_fileList = _shareFolder.list();
			
			for(int i = 0; i < _fileList.length; i++)
			{
				_st = new StringTokenizer(_fileList[i], "@");
				if(_st.nextToken().equals(_OTP))
				{
					_searchedFile = _fileList[i];
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
	}

	@Override
	public void running() throws IOException 
	{
		_activity.receive.setAllocate(30);
		_OTP = new String(_activity.receive.getByte()).trim();
		System.out.println("Receiving OTP : " + _OTP);
		OTP_Check();
		_activity.send.setPacket(_downloadFlag.getBytes(),10).write();
		_activity.send.setPacket(_downloadFlag.getBytes(),500).write();
		
		if(_temporaryFlag)
		{
			try 
			{
				File sendingFile = new File("Server_Folder\\Share" + "\\" + _searchedFile);
				StringTokenizer st_temp = new StringTokenizer(_searchedFile, "@");
				while(st_temp.hasMoreTokens())
				{
					_fileName = st_temp.nextToken();
				}
				_fileSize = sendingFile.length();
				
				_activity.send.setPacket(_fileName.getBytes(), 500).write();
				System.out.println("파일 이름(서버) : " + _fileName);
				
				_activity.send.setPacket(String.valueOf(_fileSize).getBytes(), 500).write();
				System.out.println("파일 용량(서버) : " + _fileSize);
				
				_raf = new RandomAccessFile("Server_Folder\\Share" + "\\" + _searchedFile, "rw");
				PacketProcessor p = new PacketProcessor(_raf.getChannel(), false);
				p.setAllocate(_fileSize);
				
				while(!p.isAllocatorEmpty())
				{
					_activity.send.setPacket(p.read().getByte()).write();
				}
				_raf.close();
				p.close();
				
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
		}
	}
}
