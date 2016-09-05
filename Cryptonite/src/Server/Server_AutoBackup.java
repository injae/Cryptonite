package Server;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.StringTokenizer;

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
	public Server_AutoBackup(Server_Client_Activity activity) {
		super(activity);
		// TODO �ڵ� ������ ������ ����
	}

	// protectedFolder
	private String _address = "Server_Folder\\Backup\\";
	private File _protectedFolder;
	
	// Instance
	private int _count = 1;
	
	// About File
	private String _checkProperty = null;
	private File _downloadFile = null;
	
	private String _absoluteDirectory = null;
	private long _fileSize = 0;
	private String _protectedFolderName = null;
	
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	private PacketProcessor p = null;

	private void setFolder()
	{
		_address = _address + _activity.getClientCode();
		_protectedFolder = new File(_address);
		if(!_protectedFolder.exists())
		{
			_protectedFolder.mkdirs();
		}
	}
	
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
	
	private void treeTokenizer()
	{
		StringTokenizer st = new StringTokenizer(_absoluteDirectory, "\\");
		String[] temp = new String[st.countTokens()];
		boolean check = false;
		int i = 0;
		
		while(st.hasMoreTokens())
		{
			temp[i] = st.nextToken();
			i++;
		}
		
		for(int j = 0; j < temp.length; j++)
		{
			if(check)
			{
				_address += ("\\" + temp[j]);
			}
			if(temp[j].equals(_protectedFolderName))
			{
				check = true;
			}
		}
	}
	
	private void setFileInformation(byte[] packet)
	{
		_checkProperty = "FILE";
		
		int end = 0;
		
		byte[] sizeTemp = new byte[packet[2]];
		for(int i = 0; i < sizeTemp.length; i++)
		{
			sizeTemp[i] = packet[i + 5];
			end = i+5;
		}
		_fileSize = Long.parseLong(new String(sizeTemp).trim());
		//System.out.println("���� �뷮 : " + _fileSize + " (Byte)");
		
		int max = end;
		while(packet[max] != 0)
		{
			max++;
		}
		
		byte[] addressTemp = new byte[packet[3]];
		for(int i = 0; i < addressTemp.length; i++)
		{
			addressTemp[i] = packet[i + end + 1];
		}
		_absoluteDirectory = new String(addressTemp).trim();
		
		byte[] protectedTemp = new byte[packet[4]];
		for(int i = 0; i < protectedTemp.length; i++)
		{
			protectedTemp[i] = packet[i + 900];
		}
		_protectedFolderName = new String(protectedTemp).trim();
		treeTokenizer();
		System.out.println("���� ��� : " + _address);
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		setFolder();
		if(packet[1] == DIRECTORY)
		{
			_checkProperty = "DIRECTORY";
			_packetMaxCount = 1 + 1;
		}
		else if(packet[1] == FILE)
		{
			setFileInformation(packet);
			_packetMaxCount = 1 + sendPacketSize(_fileSize);
			
			try 
			{
				_raf = new RandomAccessFile(_address, "rw");
				_fileChannel = _raf.getChannel();
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}

			p = new PacketProcessor(_fileChannel, false);
			_cutSize = 1;
		}
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) 
		{ 
			Checker(_activity.getReceiveEvent());
			if(_checkProperty.equals("DIRECTORY"))
			{
				_activity.send.setPacket(_address.getBytes(), 500).write();
			}
			else if(_checkProperty.equals("FILE"))
			{
				System.out.println("���� �뷮 : " + _fileSize + " (Byte)");
				_activity.receive.setAllocate(_fileSize);
			}
		}
		else
		{
			if(_checkProperty.equals("DIRECTORY"))
			{
				_address = new String(_activity.receive.getByte()).trim();
				System.out.println("���� ��� : " + _address);
				File newFolder = new File(_address);
				newFolder.mkdir();
				System.out.println("AUTOBACKUP COMPLETE !!");
			}
			else if(_checkProperty.equals("FILE"))
			{	
				p.setPacket(_activity.receive.getByte()).write();
				
				if(count == _packetMaxCount)
				{
					System.out.println("AUTOBACKUP COMPLETE !!");
					p.close();
				}
			}
		}
	}
}