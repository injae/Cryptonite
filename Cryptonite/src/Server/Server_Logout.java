package Server;

import java.io.IOException;

import org.omg.CORBA.ACTIVITY_COMPLETED;

/*
 * @author In Jae Lee
 * 
 */

public class Server_Logout extends Server_Funtion
{
	Server_Client_Manager _manager;
	public Server_Logout(Server_Client_Activity activity) 
	{
		super(activity);
		_manager=Server_Client_Manager.getInstance();
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
			_manager.logOut(_activity.getClientCode());
		}
		else
		{
			
		}
	}
}
