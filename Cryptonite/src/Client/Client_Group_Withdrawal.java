package Client;

import java.io.IOException;

import Function.Function;
import Function.PacketRule;

public class Client_Group_Withdrawal implements PacketRule
{

	public Client_Group_Withdrawal() {

	}
	
	public void running(String gpCode)
	{
		try 
		{
			Client_Server_Connector csc = Client_Server_Connector.getInstance();
			
			byte[] event = new byte[1024];
			event[0] = GROUP_WITHDRAWAL;
			Function.frontInsertByte(1, gpCode.getBytes(), event);
			csc.send.setPacket(event).write();
		} catch (IOException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
	}
}
