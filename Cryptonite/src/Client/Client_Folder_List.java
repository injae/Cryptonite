package Client;

import Function.PacketRule;

import java.io.IOException;
import java.util.ArrayList;

import Function.Function;

public class Client_Folder_List implements PacketRule
{
	// Instance
	
	private Client_Server_Connector _csc = null;
	private ArrayList<String> _fileList;
	
	// Constructors
	
	public Client_Folder_List()
	{
		_csc = Client_Server_Connector.getInstance();
	}
	
	// Methods
	
	public Client_Folder_List running(String path)
	{
		try 
		{
			_fileList = new ArrayList<String>();
			byte[] event = new byte[1024];
			event[0] = FOLDER_LIST;
			event[1] = (byte)path.getBytes().length;
			Function.frontInsertByte(2, path.getBytes(), event);
			_csc.send.setPacket(event).write();
			
			int fileCount = Integer.parseInt(new String(_csc.receive.read().getByte()).trim());
			
			for(int i = 0; i < fileCount; i++)
			{
				_fileList.add(new String(_csc.receive.read().getByte()).trim());
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return this;
	}
	
	public ArrayList<String> getFileList()
	{
		return _fileList;
	}
}
