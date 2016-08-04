package Server;

public class Server_User_Info 
{
	private int _user_Code = 0;
	private int _activityCode = 0;
	
	public void reset()
	{
		_user_Code = 0;
		_activityCode = 0;
	}
	
	public void setUserCode(int usercode) { _user_Code = usercode; }
	public int getUserCode() { return _user_Code; }
	
	public void setActivityCode(int activitycode) { _activityCode = activitycode; }
	public int getActivityCode() { return _activityCode; }
}
