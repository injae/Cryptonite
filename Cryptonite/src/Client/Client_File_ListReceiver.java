package Client;

import java.io.IOException;
import java.util.ArrayList;

import Function.PacketRule;

public class Client_File_ListReceiver implements PacketRule
{
	// Instance
	private byte _mod;
	private int _fileCount;
	private ArrayList<String> _fileList = null;
	
	// Another Class
	private Client_Server_Connector _csc = null;
	
	// Constructors
	public Client_File_ListReceiver()
	{
		_fileList = new ArrayList<String>();
		_csc = Client_Server_Connector.getInstance();
	}
	
	// Methods
	public void click(byte mod, String gpCode)
	{
		try 
		{
			byte[] event = new byte[1024];
			event[0] = FILE_LIST_REQUEST;
			event[1] = mod;
			if(mod == 1)	// Group
			{
				byte[] temp = gpCode.getBytes();
				for(int i = 0; i < gpCode.getBytes().length; i++)
				{
					event[i + 2] = temp[i];
				}
			}
			_csc.send.setPacket(event).write();
			
			_fileCount = Integer.parseInt(new String(_csc.receive.setAllocate(100).read().getByte()).trim());
			for(int i = 0; i < _fileCount; i++)
			{
				_fileList.add(new String(_csc.receive.setAllocate(1024).read().getByte()).trim());
				System.out.println(_fileList.get(i));
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
