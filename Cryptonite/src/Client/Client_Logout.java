package Client;

import java.io.IOException;

import Function.PacketRule;

public class Client_Logout implements PacketRule{
	
	private Client_Server_Connector _csc;
	
	public Client_Logout(){
		_csc=Client_Server_Connector.getInstance();
		
	}
	public void logout(){
		
		byte[] event=new byte[1024];
		event[0]=LOGOUT;
		try 
		{
			_csc.send.setPacket(event).write();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
