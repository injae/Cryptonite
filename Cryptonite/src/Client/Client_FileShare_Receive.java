package Client;

import java.util.*;
import java.util.concurrent.SynchronousQueue;

import Function.PacketProcesser;
import Function.PacketRule;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.net.*;
import java.io.*;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 08 - 08
 * 
 * Name : FileShare
 * Description : It can share file
 * 
 * */

public class Client_FileShare_Receive implements PacketRule
{
	// OTP Instance
	private String _OTP = "791335";
	
	// File Instance
	private String _downloadFolder = null;
	private RandomAccessFile _raf = null;
	
	private String _downloadFlag = null;
	private String _fileName = null;
	private long _fileSize = 0;
	
	// Another Class Instance
	private Client_Server_Connector _csc = null;
	private Client_FolderSelector _cfs = null;
	
	// Constructors
	public Client_FileShare_Receive() 
	{
		try 
		{
			_csc = Client_Server_Connector.getInstance();
			_cfs = new Client_FolderSelector();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void receiveFiles()
	{
		if(_OTP.length() != 6)
		{
			System.out.println("OTP length must be 6 letters.");
			System.exit(1);		// temporary
		}
		else if(_OTP.length() == 6)
		{	
			try 
			{
				_cfs.folderSelectorON();
				while(!_cfs.getSelectionEnd())
				{
					Thread.sleep(1);
				}
				_downloadFolder = _cfs.getSelectedPath();
				
				while(true)
				{					
					byte[] event = new byte[1024];
					event[0] = FILE_SHARE_SEND;
					_csc.send.setPacket(event).write();
					
					_csc.send.setPacket(_OTP.getBytes(), 30).write();	// OTP Sending	
					
					_downloadFlag = new String(_csc.receive.read().getByte()).trim();
					
					if(_downloadFlag.equals("FALSE"))
					{
						System.out.println("폴스 되었습니다.");
						break;
					}
					else if(_downloadFlag.equals("TRUE"))
					{		
						_csc.receive.setAllocate(500);
						_fileName = new String(_csc.receive.read().getByte()).trim();
						System.out.println("파일 이름 : " + _fileName);
						
						_csc.receive.setAllocate(500);
						_fileSize = Long.parseLong(new String(_csc.receive.read().getByte()).trim());
						System.out.println("파일 사이즈 : " + _fileSize);
						
						System.out.println(_csc.receive.allocatorCapacity());
						_raf = new RandomAccessFile(_downloadFolder + "\\" + _fileName, "rw");
						
						PacketProcesser p = new PacketProcesser(_raf.getChannel(), false);
						_csc.receive.setAllocate(_fileSize);
						
						while(!_csc.receive.isAllocatorEmpty())
						{
							p.setPacket(_csc.receive.read().getByteBuf()).write();
							System.out.println(_csc.receive.allocatorCapacity());
						}
						System.out.println("다읽음");
						_raf.close();
						p.close();
					}
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			catch (NullPointerException e)
			{
				System.out.println("You does not select the folder.");
				System.exit(1);
			}
			
		}
	}
}
