package Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import Function.PacketRule;
import Function.Function;

public class Client_Group_Search implements PacketRule
{
	// Instance
	private String _searchID;
	private ArrayList<String> _id;
	
	// Another Class Instance
	private Client_Server_Connector _csc = null;
	
	// Constructors
	public Client_Group_Search()
	{
		_csc = Client_Server_Connector.getInstance();
	}
	
	// Methods
	public void search(String id)
	{
		_searchID = id;
		
		try 
		{
			byte[] event = new byte[1024];
			event[0] = GROUP_SEARCH;
			event[1] = (byte)_searchID.getBytes().length;
			Function.frontInsertByte(2, _searchID.getBytes(), event);
			_csc.send.setPacket(event).write();
			
			String choice = new String(_csc.receive.setAllocate(1024).read().getByte()).trim();
			if(!choice.equals("FALSE"))
			{
				for(int i = 0; i < Integer.parseInt(choice); i++)
				{
					_id.add(new String(_csc.receive.setAllocate(1024).read().getByte()));
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String[] getID()
	{
		String[] temp = new String[_id.size()];
		for(int i = 0; i < temp.length; i++)
		{
			temp[i] = _id.get(i).trim();
		}
		
		return temp;
	}
	
	public void setDefault()
	{
		_id = new ArrayList<String>();
		_searchID = null;
	}
}