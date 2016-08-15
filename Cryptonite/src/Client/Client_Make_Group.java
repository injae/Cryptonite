package Client;

import java.io.IOException;

public class Client_Make_Group 
{
	Client_Server_Connector _csc;
	
	public Client_Make_Group() 
	{
		_csc = Client_Server_Connector.getInstance();
	}
	
	public void make(String groupmember)
	{
		try 
		{
			
			
			_csc.send.setPacket(groupmember.getBytes(),1024).write();
		} catch (IOException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}
}
