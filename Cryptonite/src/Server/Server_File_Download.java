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
	RandomAccessFile raf;
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
			raf = new RandomAccessFile(file, "rw");
			_ps = new PacketProcessor(raf.getChannel(), false);
			_ps.setAllocate(_fileSize);

			Server_Thread_Manager.getInstance().register(new Server_Funtion_Thread(_activity) 
			{		
				@Override
				public void loop() throws Exception 
				{
					_activity.send.setPacket(_ps.read().getByte()).write();
				}
				
				@Override
				public void delete() throws Exception
				{
					_ps.close();
				}
				
				@Override
				public boolean breakPoint() throws Exception
				{
					return !_ps.isAllocatorEmpty();
				}
			});
		}
	}
}
