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
		_id = new ArrayList<String>();
	}
	
	// Methods
	public void search()
	{
		_searchID = Client_Group_Main.givename();
		
		try 
		{
			byte[] event = new byte[1024];
			event[0] = GROUP_SEARCH;
			event[1] = (byte)_searchID.getBytes().length;
			Function.frontInsertByte(2, _searchID.getBytes(), event);
			_csc.send.setPacket(event).write();
			
			String choice = new String(_csc.receive.setAllocate(1024).read().getByte()).trim();
			if(choice.equals("FALSE"))
			{
				//System.out.println("존재하는 ID가 없습니다.");
				showMessage("ID Error","존재하는 ID가 없습니다.");
			}
			else
			{
				for(int i = 0; i < Integer.parseInt(choice); i++)
				{
					_id.add(new String(_csc.receive.setAllocate(1024).read().getByte()));
					System.out.println("결과 : " + _id.get(i).trim());
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
			temp[i] = _id.get(i);
		}
		
		return temp;
	}
	
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
