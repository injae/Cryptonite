package Server;

import java.io.IOException;

import org.omg.CORBA.ACTIVITY_COMPLETED;

public class Server_Logout extends Server_Funtion{

	public Server_Logout(Server_Client_Activity activity) {
		super(activity);
		// TODO 磊悼 积己等 积己磊 胶庞
	}

	Server_Client_Activity _activity;
	Server_Client_Manager _manager;
	@Override
	public void Checker(byte[] packet)
	{
		_packetMaxCount = 2;
		_manager=Server_Client_Manager.getInstance();
		_activity.receive.setAllocate(2);
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			_activity.receive.getByte();
			_manager.logOut(_activity.getClientCode());
		}
	}
}
