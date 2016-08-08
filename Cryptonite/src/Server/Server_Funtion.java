/**
 * 
 */
package Server;

/**
 * @author In Jae Lee
 *
 */
public abstract class Server_Funtion
{	
	public int _packetMaxCount = 0;
	protected int _packetCutSize = 10;
	
	public int getLimitSize() { return _packetCutSize; }
	
	public abstract void Checker(byte[] packet);
	public abstract void running(Server_Client_Activity activity);
}
