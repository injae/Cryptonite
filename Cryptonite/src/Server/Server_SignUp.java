package Server;

public class Server_SignUp extends Server_Funtion 
{
	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = packet[1];
	}

	@Override
	public void running(Server_Client_Activity activity) 
	{
		System.out.println("SignUp running");
		
	/*	for(int i =0; i < _packetMaxCount-1; i++)
		{
			byte[] buf = activity._receiveQueue.remove();
			System.out.println("get: " +buf.length);
		}*/
	}
}
