package Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;

import Function.PacketProcessor;
import Function.PacketRule;

public class Server_File_Upload extends Server_Funtion implements PacketRule
{
	// Instance
	private String _address;
	private String _fileName;
	private long _fileSize;
	private String _gpCode;
	
	private PacketProcessor _p = null;
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	
	public Server_File_Upload(Server_Client_Activity activity) 
	{
		super(activity);
	}
	
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
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(packet[1]);
		int end = 0;
		byte[] nameTemp = new byte[packet[1]];
		for(int i = 0; i < nameTemp.length; i++)
		{
			nameTemp[i] = packet[i + 3];
			end = i + 3;
		}
		bb.put(nameTemp); bb.flip();
		_fileName = cs.decode(bb).toString();
		
		byte[] sizeTemp = new byte[packet[2]];
		for(int i = 0; i < sizeTemp.length; i++)
		{
			sizeTemp[i] = packet[i + end + 1];
		}
		_fileSize = Long.parseLong(new String(sizeTemp).trim());
		
		byte[] gpCodeTemp = new byte[100];
		for(int i = 0; i < gpCodeTemp.length; i++)
		{
			gpCodeTemp[i] = packet[i + 800];
		}
		_gpCode = new String(gpCodeTemp).trim();
	}

	@Override
	public void Checker(byte[] packet) 
	{
		setFileInformation(packet);
		_packetMaxCount = 1 + sendPacketSize(_fileSize);
		
		try 
		{
			_address = "Server_Folder\\Backup\\" + _gpCode;
			_raf = new RandomAccessFile(_address + "\\" + _fileName, "rw");
			_fileChannel = _raf.getChannel();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		_activity.receive.setAllocate(_fileSize);
		
		_p = new PacketProcessor(_fileChannel, false);
		_cutSize = 1;
	}

	@Override
	public void running(int count) throws IOException
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			_p.setPacket(_activity.receive.getByte()).write();
			
			if(count == _packetMaxCount)
			{
				_p.close();
			}
		}
	}
}
