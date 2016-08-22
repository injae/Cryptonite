package Client;

import Function.PacketRule;

import java.io.IOException;

import Function.Function;

public class Client_Delete_Group implements PacketRule
{
	// Instance
	
	// Another Class Instance
	private Client_Server_Connector _csc = null;
	
	// Constructors
	public Client_Delete_Group()
	{
		_csc = Client_Server_Connector.getInstance();
	}
	
	// Methods
	public void deleteGroup(String gpCode)
	{
		try 
		{
			byte[] event = new byte[1024];
			event[0] = DELETE_GROUP;
			Function.frontInsertByte(1, gpCode.getBytes(), event);
			_csc.send.setPacket(event).write();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}