package Client;

import Function.PacketProcessor;
import Function.PacketRule;

import java.io.*;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 08 - 08
 * 
 * Name : FileShare
 * Description : It can share file
 * 
 * */

public class Client_FileShare_Receive extends Thread implements PacketRule
{
	// OTP Instance
	private String _OTP;
	
	private static long _filesize = 0;
	
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
	private Client_Progressbar _cpb = null;
	
	// Constructors
	public Client_FileShare_Receive() 
	{
		_csc = Client_Server_Connector.getInstance();
		_cfs = new Client_FolderSelector();
		_cpb = new Client_Progressbar(2);
	}
	
	public void folderSelect()
	{
		_cfs.folderSelectorON();
		while(!_cfs.getSelectionEnd())
		{
			try 
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		_downloadFolder = _cfs.getSelectedPath();
	}
	
	public static long receive_filesize()
	{
		return _filesize;
	}
	
	public void setOTP(String OTP)
	{
		_OTP = OTP;
	}
	
	public void run()
	{
		Charset cs = Charset.forName("UTF-8");
		if(_OTP.length() != 6)
		{
			showMessage("Notification", "OTP length must be 6 letters.");
		}
		else if(_OTP.length() == 6)
		{	
			try 
			{	
				_cpb.UI_ON();
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
						break;
					}
					else if(_downloadFlag.equals("TRUE"))
					{		
						_csc.receive.setAllocate(500);
						_fileName = cs.decode(_csc.receive.read().getByteBuf()).toString().trim();
						System.out.println("File Name : " + _fileName);
						
						_csc.receive.setAllocate(500);
						_fileSize = Long.parseLong(new String(_csc.receive.read().getByte()).trim());
						System.out.println("File Size : " + _fileSize);
				
						_raf = new RandomAccessFile(_downloadFolder + "\\" + _fileName, "rw");
						
						p = new PacketProcessor(_raf.getChannel(), false);
						_csc.receive.setAllocate(_fileSize);
						
						while(!_csc.receive.isAllocatorEmpty())
						{
							p.setPacket(_csc.receive.read().getByte()).write();
						}
						p.close();
						_filesize+=_fileSize;
					}
				}
				_cpb.UI_OFF();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			catch (NullPointerException e)
			{
				System.out.println("You does not select the folder.");
			}
		}
	}
	
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
