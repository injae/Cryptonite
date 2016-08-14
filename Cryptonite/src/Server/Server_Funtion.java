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
	protected int _packetCutSize = 10;
	protected Server_Client_Activity _activity;
	
	public int getLimitSize() { return _packetCutSize; }
	
	public abstract void Checker(byte[] packet, Server_Client_Activity activity);
	public abstract void running() throws IOException;
}
