package Server;

import java.io.IOException;

/*
 * @author In Jae Lee
 * 
 */

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
		if(count == 1)
		{
			Checker(_activity.getReceiveEvent());
			if(_activity._eventQueue.isEmpty())
			{
				byte[] event = new byte[1024];
				event[0] = -1;
				_activity.send.setPacket(event).write();
			}
			else
			{
				_activity.send.setPacket(_activity._eventQueue.remove()).write();
			}
		}
		else
		{

		}
	}
}
