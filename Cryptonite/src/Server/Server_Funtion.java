/**
 * 
 */
package Server;

import java.io.IOException;

/**
 * @author In Jae Lee
 *
 */
public abstract class Server_Funtion
{	
	public int _packetMaxCount = 0;
	protected Server_Client_Activity _activity;
	
	public Server_Funtion(Server_Client_Activity activity)
	{
		_activity = activity;
	}
	
	public abstract void Checker(byte[] packet);
	public abstract void running(int count) throws IOException;
}
