package Client;

import java.io.IOException;
import Function.PacketRule;

public class Client_Make_Group implements PacketRule
{
	Client_Server_Connector _csc;
	
	public Client_Make_Group() 
	{
		_csc = Client_Server_Connector.getInstance();
	}
	
	public void make(String[] groupmember)
	{
		try 
		{
			byte[] event = new byte[1024];
			event[0] = MAKE_GROUP;
			event[1] = (byte)(1 + groupmember.length);
			_csc.send.setPacket(event).write();
			
			for(int i = 0; i < groupmember.length; i++)
			{
				_csc.send.setPacket(groupmember[i].getBytes(), 500).write();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
