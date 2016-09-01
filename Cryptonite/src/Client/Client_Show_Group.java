package Client;

import Function.PacketRule;

import java.io.IOException;
import java.util.ArrayList;

import Function.Function;

public class Client_Show_Group implements PacketRule
{
	// Instance
	private Client_Server_Connector _csc = null;
	private String _id;
	private int _groupCount;
	private ArrayList<String> _groupNameArray;
	
	// Constructors
	public Client_Show_Group()
	{
		_csc = Client_Server_Connector.getInstance();
		_groupNameArray = new ArrayList<String>();
	}
	
	// Methods
	public void running(String id)
	{
		try 
		{
			_id = id;
			byte[] event = new byte[1024];
			event[0] = SHOW_GROUP;
			event[1] = (byte)_id.getBytes().length;
			Function.frontInsertByte(2, _id.getBytes(), event);
			_csc.send.setPacket(event).write();
			
			_groupCount = Integer.parseInt(new String(_csc.receive.setAllocate(100).read().getByte()).trim());
			
			for(int i = 0; i < _groupCount; i++)
			{
				_groupNameArray.add(new String(_csc.receive.setAllocate(1024).read().getByte()).trim());
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String[] getGroupName()
	{
		String[] temp = new String[_groupNameArray.size()];
		
		for(int i = 0; i < temp.length; i++)
		{
			temp[i] = _groupNameArray.get(i);
		}
		
		return temp;
	}
}
