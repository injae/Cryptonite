package Server;

import java.io.IOException;

public class Server_Send_Event extends Server_Funtion
{

	public Server_Send_Event(Server_Client_Activity activity)
	{
		super(activity);
	}

	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = 1;
	}

	@Override
	public void running(int count) throws IOException 
	{
		_activity.send.setPacket(_activity._eventQueue.remove());
	}
}
