package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server_Group_Search extends Server_Funtion 
{
	// Instance
	private String _searchID;
	private int _idCount = 0;
	private ArrayList<String> _id = null;
	
	// Constructors
	public Server_Group_Search(Server_Client_Activity activity) 
	{
		super(activity);
	}
	
	// Methods
	private void setID(byte[] packet)
	{
		byte[] idTemp = new byte[packet[1]];
		for(int i = 0; i < idTemp.length; i++)
		{
			idTemp[i] = packet[i + 2];
		}
		_searchID = new String(idTemp).trim();
	}

	@Override
	public void Checker(byte[] packet) 
	{
		_id = new ArrayList<String>();
		setID(packet);
		_packetMaxCount = 1;
		_cutSize = 1;
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) 
		{ 
			Checker(_activity.getReceiveEvent());
			try 
			{
				Server_DataBase db = Server_DataBase.getInstance();
				ResultSet rs = db.Query("select * from test where id like '%" + _searchID + "%';");
				if(!rs.next())
				{
					_activity.send.setPacket("FALSE".getBytes(), 1024).write();
				}
				else
				{
					_idCount++;
					_id.add(rs.getString(2));
					
					while(rs.next())
					{
						_idCount++;
						_id.add(rs.getString(2));
					}
					
					_activity.send.setPacket(String.valueOf(_idCount).getBytes(), 1024).write();
					for(int i = 0; i < _idCount; i++)
					{
						_activity.send.setPacket(_id.get(i).getBytes(), 1024).write();
					}
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
