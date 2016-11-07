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
	public Server_File_Download(Server_Client_Activity activity) 
	{
		super(activity);
	}

	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = 1 + 1;
		_cutSize = 1;
		_activity.receive.setAllocate(500);
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			String filename = new String(_activity.receive.getByte()).trim();
			if (filename.contains("$"))	//group file download
			{
				boolean check = new Server_Check_GPS().check(filename, _activity);
				if (check == false) {return;}
			}
			
			File file = new File(filename);
			
			_fileSize = file.length();
			
			_activity.send.setPacket(String.valueOf(_fileSize).getBytes(),500).write();
			
			raf = new RandomAccessFile(file, "rw");
			_ps = new PacketProcessor(raf.getChannel(), false);
			
			
			if(filename.substring(filename.length()-5, filename.length()).equals(".cnmc"))
				_ps.setAllocate(64).setAllocate(_fileSize-64);
			else
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
