package Server;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Function.PacketProcessor;

public class Server_Make_Group extends Server_Funtion
{
	public Server_Make_Group(Server_Client_Activity activity) {
		super(activity);
		_db = Server_DataBase.getInstance();
		_manager = Server_Client_Manager.getInstance();
		_members = new ArrayList<String>();
	}

	private int _groupMemberSize = 0;
	private Server_Client_Activity _activity;
	private Server_Client_Manager _manager = null;
	private Server_DataBase _db = null;
	private ArrayList<String> _members = null;


	
	@Override
	public void Checker(byte[] packet) 
	{
		System.out.println("packet[1] : " + packet[1]);
		_packetMaxCount = packet[1];
		_groupMemberSize = packet[1] - 1;
		
		for(int i = 0; i < _groupMemberSize; i++)
		{
			_activity.receive.setAllocate(500);
		}
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			String gpCode = _manager._code_manager.getGpCode();
			String memberSet = new String();
			int code = Integer.parseInt(gpCode.substring(1));
			int ccount = 0;
			
			for(int i = 0; i < _groupMemberSize; i++)
			{
				try 
				{
					ResultSet rs = _db.Query("select * from test where id like'" + new String(_activity.receive.getByte()).trim() + "';");
					
					if(rs.next())
					{
						_members.add("@" + rs.getInt(6));
						if(ccount == 0)
						{
							memberSet = _members.get(ccount);
						}
						else
						{
							memberSet = memberSet + ":" + _members.get(ccount);
						}
						ccount++;
					}
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
			_db.Update("insert into grouplist values(" + code + ",'" + memberSet + "');");
			
			for(int i = 0; i < _members.size(); i++)
			{
				ResultSet rs = _db.Query("select *from test where uscode = '" + _members.get(i) + "';");
				String groupList = null;
				try 
				{
					if(rs.next())
					{
						groupList =  rs.getString(10) + groupList + ":" + gpCode;
					}
					else
					{
						groupList = gpCode;
					}
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
				_db.Update("update test set mygrouplist = '" + groupList + "' where uscode = " + Integer.parseInt(_members.get(i).substring(1)) + ";");
			}
			File newFolder = new File("Server_Folder/Backup/" + gpCode);
			if(!newFolder.exists())
			{
				newFolder.mkdir();
			}
		}
	}
}
