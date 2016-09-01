package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Server_Show_Group extends Server_Funtion
{
	// Instance
	private String _id;
	private ArrayList<String> _groupCodeArray;
	
	// Constructors
	public Server_Show_Group(Server_Client_Activity activity) 
	{
		super(activity);
	}

	// Methods
	@Override
	public void Checker(byte[] packet) 
	{
		byte[] idTemp = new byte[packet[1]];
		for(int i = 0; i < idTemp.length; i++)
		{
			idTemp[i] = packet[i + 2];
		}
		_id = new String(idTemp).trim();
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) 
		{
			Checker(_activity.getReceiveEvent());
			_groupCodeArray = new ArrayList<String>();
			
			try 
			{
				Server_DataBase db = Server_DataBase.getInstance();
				ResultSet rs = db.Query("select * from test where id = '" + _id + "';");
				rs.next();
				String myGroupList = rs.getString(10);
				StringTokenizer st = new StringTokenizer(myGroupList, ":");
				while(st.hasMoreTokens())
				{
					_groupCodeArray.add(st.nextToken());
				}
				
				_activity.send.setPacket(String.valueOf(_groupCodeArray.size()).getBytes(), 100).write();
				
				for(int i = 0; i < _groupCodeArray.size(); i++)
				{
					ResultSet rs2 = db.Query("select * from grouplist where gpcode = " + Server_Code_Manager.codeCutter(_groupCodeArray.get(i)) + ";");
					rs2.next();
					_activity.send.setPacket(rs2.getString(3).getBytes(), 1024).write();
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			
		}
	}

}
