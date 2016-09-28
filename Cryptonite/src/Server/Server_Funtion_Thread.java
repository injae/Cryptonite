package Server;

public abstract class Server_Funtion_Thread 
{
	public Server_Client_Activity _activity;
	
	public Server_Funtion_Thread(Server_Client_Activity activity) 
	{
		_activity = activity;
		_activity.addPacketCount();
		System.out.println("regist" + _activity.getPakcetCount() + " " + _activity._funtionList.getFirst()._packetMaxCount);
	}

	final public void start()
	{
		try 
		{
			if(breakPoint()) 
			{
				_activity.addPacketCount();
				loop();
				Server_Thread_Manager.getInstance().register(this);
			}
			else 
			{
				_activity._funtionList.getFirst()._packetMaxCount = _activity.getPakcetCount();
				delete(); 
				System.out.println("thread");
				_activity.finishCheck();
			}
			
		} catch (Exception e) 
		{
			e.printStackTrace();
			Server_Administrator.getInstance().errorUpdate(e.getMessage());
			Server_Client_Manager.getInstance().stopManaging(_activity.getClientCode());
		}
	}
	
	abstract public boolean breakPoint() throws Exception;
	abstract public void loop() throws Exception;
	abstract public void delete() throws Exception;
}
