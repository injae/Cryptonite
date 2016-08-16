package Server;

import java.io.IOException;

public class Server_ImDie extends Server_Funtion
{

	public Server_ImDie(Server_Client_Activity activity) {
		super(activity);
		// TODO 磊悼 积己等 积己磊 胶庞
	}

	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = 1;
		Server_Client_Manager.getInstance().requestManage(_activity.getClientCode());
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			Server_Client_Manager.getInstance().stopManaging(_activity.getClientCode());
			System.out.println("im die");
		}
	}
	
}
