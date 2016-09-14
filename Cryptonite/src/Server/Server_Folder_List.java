package Server;

import java.io.File;
import java.io.IOException;

public class Server_Folder_List extends Server_Funtion {

	// Instance
	private String _address;
	private File _folder;
	
	// Constructors
	
	public Server_Folder_List(Server_Client_Activity activity) 
	{
		super(activity);
	}
	
	// Methods
	
	private void setInformation(byte[] packet)
	{
		byte[] addressTemp = new byte[packet[1]];
		for(int i = 0; i < addressTemp.length; i++)
		{
			addressTemp[i] = packet[i + 2];
		}
		_address = new String(addressTemp).trim();
		
		_folder = new File(_address);
	}

	@Override
	public void Checker(byte[] packet) 
	{
		setInformation(packet);
		_packetMaxCount = 1;
		_cutSize = 1;
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) 
		{ 
			Checker(_activity.getReceiveEvent());
			String[] fileList = _folder.list();
			_activity.send.setPacket(String.valueOf(fileList.length).getBytes(), 1024).write();
			
			for(int i = 0; i < fileList.length; i++)
			{
				_activity.send.setPacket(fileList[i].getBytes(), 1024).write();
			}
		}
		else
		{
			
		}
	}

}
