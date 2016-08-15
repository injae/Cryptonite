package Client;

import Function.PacketProcessor;
import Function.PacketRule;

import java.io.*;
import java.nio.charset.Charset;

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
	private String _OTP;
	
	// File Instance
	private String _downloadFolder = null;
	private RandomAccessFile _raf = null;
	
	private String _downloadFlag = null;
	private String _fileName = null;
	private long _fileSize = 0;
	private PacketProcessor p = null;
	
	// Another Class Instance
	private Client_Server_Connector _csc = null;
	private Client_FolderSelector _cfs = null;
	
	// Constructors
	public Client_FileShare_Receive() 
	{
		_csc = Client_Server_Connector.getInstance();
		_cfs = new Client_FolderSelector();
	}
	
	public void receiveFiles(String OTP)
	{
		Charset cs = Charset.forName("UTF-8");
		_OTP = OTP;
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
					_csc.receive.setAllocate(500);

					_downloadFlag = new String(_csc.receive.read().getByte()).trim();
					if(_downloadFlag.equals("FALSE"))
					{
						System.out.println("FALSE has occured !!");
						break;
					}
					else if(_downloadFlag.equals("TRUE"))
					{		
						System.out.println("엘스이프");
						_csc.receive.setAllocate(500);
						_fileName = cs.decode(_csc.receive.read().getByteBuf()).toString().trim();
						System.out.println("파일 이름 : " + _fileName);
						
						_csc.receive.setAllocate(500);
						_fileSize = Long.parseLong(new String(_csc.receive.read().getByte()).trim());
						System.out.println("파일 사이즈 : " + _fileSize);
						
						_raf = new RandomAccessFile(_downloadFolder + "\\" + _fileName, "rw");
						
						p = new PacketProcessor(_raf.getChannel(), false);
						_csc.receive.setAllocate(_fileSize);
						
						while(!_csc.receive.isAllocatorEmpty())
						{
							p.setPacket(_csc.receive.read().getByte()).write();
						}
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
			}
			
		}
	}
}
