package Client;

import java.io.IOException;

import Function.PacketRule;

public class Client_Receive_Event implements PacketRule
{
	private Client_Server_Connector _csc;
	
	public Client_Receive_Event() 
	{
		_csc = Client_Server_Connector.getInstance();
	}
	
	public void getEvent()
	{
		try 
		{
			byte[] sevent = new byte[1024];
			sevent[0] = EVENT;
			_csc.send.setPacket(sevent).write();			
			
			byte[] revent = _csc.receive.read().getByte();
			
			
			
		} 
		catch (IOException e)
		{		
			e.printStackTrace();
		}
	}
}
