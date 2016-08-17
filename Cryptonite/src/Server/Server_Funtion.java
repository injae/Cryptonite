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
	protected Server_Client_Activity _activity;
	public Server_Funtion(Server_Client_Activity activity) { _activity = activity; }

	protected int _cutSize = Server_Client_Activity.LIMIT_PACKET;
	private int _cutcount = 0;
	public boolean isCut(int packetCount)
	{
		_cutcount++;
		if(_cutcount == _cutSize)
		{
			_cutcount = 0;
			return true;
		}
		return false;
	}
	
	public int _packetMaxCount = 0;
	public abstract void Checker(byte[] packet);
	public abstract void running(int count) throws IOException;
}
