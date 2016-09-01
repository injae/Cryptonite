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
	
	public byte[] getEvent()
	{
		byte[] revent = null;
		try 
		{
			byte[] sevent = new byte[1024];
			sevent[0] = EVENT;
			_csc.send.setPacket(sevent).write();
			
			revent = _csc.receive.read().getByte();
			System.out.print("event code: "+revent[0] + revent[1] + revent[2]+" ");
		} 
		catch (IOException e)
		{		
			e.printStackTrace();
		}
		return revent;
	}
}
