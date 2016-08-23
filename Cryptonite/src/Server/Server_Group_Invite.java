package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server_Group_Invite extends Server_Funtion 
{
	private String _id;
	private String _gpCode;
	
	public Server_Group_Invite(Server_Client_Activity activity) 
	{
		super(activity);
	}

	private void setInformation(byte[] packet)
	{
		int end = 0;
		byte[] idTemp = new byte[packet[1]];
		for(int i = 0; i < idTemp.length; i++)
		{
			idTemp[i] = packet[i + 3];
			end = i + 3;
		}
		_id = new String(idTemp).trim();
		System.out.println("아이디 : " + _id);
		
		byte[] gpCodeTemp = new byte[packet[2]];
		for(int i = 0; i < gpCodeTemp.length; i++)
		{
			gpCodeTemp[i] = packet[i + end + 1];
		}
		_gpCode = new String(gpCodeTemp).trim();
		System.out.println("그룹코드 : " + _gpCode);
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		setInformation(packet);
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
				ResultSet rs = db.Query("select * from test where id = '" + _id + "';");
				rs.next();
				String uscode = "@" + rs.getString(6);
				
				ResultSet rs2 = db.Query("select * from grouplist where gpcode = " + Server_Code_Manager.codeCutter(_gpCode) + ";");
				rs2.next();
				String gplist = rs2.getString(2);
				gplist += ":" + uscode;
				db.Update("update grouplist set gplist = '" + gplist + "' where gpcode = " + Server_Code_Manager.codeCutter(_gpCode) + ";");
				
				ResultSet rs3 = db.Query("select * from test where id = '" + _id + "';");
				rs3.next();
				String mygrouplist = rs3.getString(10);
				if(mygrouplist.equals("NULL"))
				{
					mygrouplist = _gpCode;
				}
				else
				{
					mygrouplist = ":" + _gpCode;
				}
				db.Update("update test set mygrouplist = '" + mygrouplist + "' where uscode = " + Server_Code_Manager.codeCutter(uscode) + ";");
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
