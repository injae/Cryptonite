package Client;

import java.util.*;

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
	private String _OTP = "413588";
	
	// File Instance
	private String _downloadFolder = null;
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	
	private String _downloadFlag = null;
	private int _fileCount = 0;
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
				
				byte[] event = new byte[1024];
				event[0] = FILE_SHARE_SEND;
				_csc.send.setPacket(event).write();
				_csc.send.setPacket(_OTP.getBytes()).write();	// OTP Sending				
				
			
				byte[] flagByte = _csc.receive.read().getByte();
				_downloadFlag = new String(flagByte).trim();
				
				if(_downloadFlag.equals("FALSE"))
				{
					System.out.println("There is no OTP which is equal.");
					System.exit(1);	// Temporary
				}
				else if(_downloadFlag.equals("TRUE"))
				{
					
					System.out.println("OTP is correct!!");
					byte[] temp = _csc.receive.read().getByte();
					_fileCount = Integer.parseInt(new String(temp).trim());
					System.out.println("fileCount : " + _fileCount);
					
					for(int i = 0; i < _fileCount; i++)
					{
						temp = _csc.receive.read().getByte();
						_fileName = new String(temp).trim();
						System.out.println("파일 이름 : " + _fileName);
						
						temp = _csc.receive.read().getByte();
						_fileSize = Long.parseLong(new String(temp).trim());
						System.out.println("파일 사이즈 : " + _fileSize);
						//System.out.println("나누기 : " + (_fileSize / 1024));
						//_csc.receive.setAllocate(_fileSize);
						
						_raf = new RandomAccessFile(_downloadFolder + "\\" + _fileName, "rw");
						_fileChannel = _raf.getChannel();
						PacketProcesser p = new PacketProcesser(_fileChannel, false);
						_csc.receive.setAllocate(_fileSize);
						
						//int count = 0;
						while(!_csc.receive.isAllocatorEmpty())
						{
							//count++;
							//System.out.println(count);
							p.setPacket(_csc.receive.read().getByteBuf()).write();
						}
						p.close();
						//System.out.println("튀어나옴");
						
						/*ByteBuffer buffer;
						buffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
						while(_fileSize > 0)
						{														
							buffer = _csc.receive.read().getByteBuf();
							//System.out.println(buffer);
							_fileSize -= FILE_BUFFER_SIZE;
							//buffer.flip();
							while(!_fileChannel.isOpen())
							{
								Thread.sleep(1);
							}
							_fileChannel.write(buffer);
							
							if(_fileSize <= 0)
							{
								break;
							}
						}*/
						
						//_fileChannel.close();
						_raf.close();
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
