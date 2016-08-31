package Client;

import java.io.IOException;
import java.util.Scanner;
import Function.PacketRule;
import Function.Function;

public class Client_Group_Invite implements PacketRule
{
	// Instance
	private String _id;
	
	// Another Class Instance
	private Client_Server_Connector _csc = null;
	
	// Constructors
	public Client_Group_Invite()
	{
		_csc = Client_Server_Connector.getInstance();
	}
	
	// Methods
	public void running(String id, String gpCode)
	{
		_id = id;
		
		try 
		{
			byte[] event = new byte[1024];
			event[0] = GROUP_INVITE;
			event[1] = (byte)_id.getBytes().length;
			event[2] = (byte)gpCode.getBytes().length;
			Function.frontInsertByte(3, _id.getBytes(), event);
			Function.frontInsertByte(3 + _id.getBytes().length, gpCode.getBytes(), event);
			_csc.send.setPacket(event).write();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
