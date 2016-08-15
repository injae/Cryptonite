package Server;

import java.io.IOException;

public class Server_ImDie extends Server_Funtion
{

	@Override
	public void Checker(byte[] packet, Server_Client_Activity activity) 
	{
		_packetCutSize = 1;
		_packetMaxCount = 1;
		Server_Client_Manager.getInstance().requestManage(activity.getClientCode());
	}

	@Override
	public void running() throws IOException 
	{
		Server_Client_Manager.getInstance().stopManaging(_activity.getClientCode());
		System.out.println("im die");
	}
	
}
