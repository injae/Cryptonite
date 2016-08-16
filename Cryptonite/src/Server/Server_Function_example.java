package Server;

import java.io.IOException;

public class Server_Function_example extends Server_Funtion
{
	public Server_Function_example(Server_Client_Activity activity) 
	{
		super(activity);
	}

	@Override
	public void Checker(byte[] packet)
	{
		//_packetMaxCount = /*your packet count*/
		// if you receive packet _activty.setAllocate(); is important  if you not allocate your packet size your function is not running
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			// your code
			// if you send packet to client	_activity.send.setpacket().write();
			// if you receive packet to client _activity.receive.getByte();
		}
	}

}
