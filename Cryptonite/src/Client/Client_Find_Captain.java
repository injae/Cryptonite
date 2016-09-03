package Client;

import Function.PacketRule;

import java.io.IOException;

import Function.Function;

public class Client_Find_Captain implements PacketRule
{
	// Instance
	private String _choice;
	
	// Another Instance
	private Client_Server_Connector _csc = null;
	
	// Constructors
	public Client_Find_Captain()
	{
		_csc = Client_Server_Connector.getInstance();
	}
	
	// Methods
	public String running(String gpCode, String id)
	{
		try 
		{
			byte[] event = new byte[1024];
			event[0] = FIND_CAPTAIN;
			event[1] = (byte)gpCode.getBytes().length;
			event[2] = (byte)id.getBytes().length;
			Function.frontInsertByte(3, gpCode.getBytes(), event);
			Function.frontInsertByte(3 + gpCode.getBytes().length, id.getBytes(), event);
			_csc.send.setPacket(event).write();
			
			_choice = new String(_csc.receive.setAllocate(1024).read().getByte()).trim(); // TRUE = CAPTAIN, FALSE = CREW
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return _choice;
	}
}
