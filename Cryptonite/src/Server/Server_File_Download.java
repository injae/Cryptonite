package Server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import Function.PacketProcessor;

public class Server_File_Download extends Server_Funtion 
{
	private long _fileSize;
	private PacketProcessor _ps;
	
	public Server_File_Download(Server_Client_Activity activity) {
		super(activity);
	}

	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = 1+1;
		_cutSize = 1;
		_activity.receive.setAllocate(500);
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			Charset cs = Charset.forName("UTF-8");
			
			ByteBuffer bb = ByteBuffer.allocate(500);
			bb.put(_activity.receive.getByte()); bb.flip();
			File file = new File(cs.decode(bb).toString().trim());
			_fileSize = file.length();
			
			_activity.send.setPacket(String.valueOf(_fileSize).getBytes(),500).write();
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			_ps = new PacketProcessor(raf.getChannel(), false);
			_ps.setAllocate(_fileSize);
			new File_Send().start();
		}
	}
	
	class File_Send extends Thread
	{
		public void run()
		{
			while(!_ps.isAllocatorEmpty())
			{
				try {
					_activity.send.setPacket(_ps.read().getByte()).write();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			_ps.close();
		}
	}
}
