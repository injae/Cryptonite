/**
 * 
 */
package Server;

/**
 * @author user
 *
 */
public abstract class Server_Funtion
{	
	public int _packetMaxCount = 0;
	
	public abstract void Checker(byte[] packet);
	public abstract void running(Server_Client_Activity activity);

}
