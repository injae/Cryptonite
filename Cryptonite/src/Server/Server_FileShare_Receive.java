package Server;

import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Vector;

import javax.swing.plaf.synth.SynthSpinnerUI;

import java.io.*;
import Function.PacketRule;
import Function.PacketProcessor;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 29
 * 
 * Name : FileShare
 * Description : It can share file
 * 
 * */

public class Server_FileShare_Receive extends Server_Funtion implements PacketRule
{
	public Server_FileShare_Receive(Server_Client_Activity activity) {
		super(activity);
	}
	
	// Address
	private String _address = "Server_Folder\\Share";
	
	// Instance
	private int _count = 1;
	private int _fileCount = 0;
	private String _fileName = null;
	private long _fileSize = 0;
	
	// OTP Instance
	private int _oneTime = 1;
	private String _OTP = "";
	private Vector<String> _OTP_List = null;
	private boolean _checkOTP = false;
	private FileOutputStream fos = null;
	private ObjectOutputStream oos = null;
	
	// FileChannel and RandomAccessFile
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	PacketProcessor p = null;
	
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
		_fileCount = packet[1];
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
		
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(nameTemp.length);
		bb.put(nameTemp);
		bb.flip();
		_fileName = cs.decode(bb).toString().trim();

		System.out.println("파일 이름 : " + _fileName);
		System.out.println("파일 용량 : " + _fileSize + " (Byte)");
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		setFileInformation(packet);
		_packetMaxCount = 1 + sendPacketSize(_fileSize);
		
		System.out.println(_fileName);
		try 
		{
			_raf = new RandomAccessFile(_address + "\\" + _fileName, "rw");
			_fileChannel = _raf.getChannel();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		_activity.receive.setAllocate(_fileSize);
		
		p = new PacketProcessor(_fileChannel, false);
		_cutSize = 1;
	}

	@Override
	public void running(int count) throws IOException
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			p.setPacket(_activity.receive.getByte()).write();
			
			if(count == _packetMaxCount)
			{
				System.out.println(_fileName + " 파일이 수신 완료되었습니다.");
				p.close();
			}
		}
	}
}
