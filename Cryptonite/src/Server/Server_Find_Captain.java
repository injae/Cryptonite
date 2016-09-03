package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class Server_Find_Captain extends Server_Funtion
{
	// Instance
	private String _gpCode;
	private String _id;
	
	// Constructors
	public Server_Find_Captain(Server_Client_Activity activity) 
	{
		super(activity);
	}

	// Methods
	private void setInformation(byte[] packet)
	{
		int end = 0;
		byte[] gpCodeTemp = new byte[packet[1]];
		for(int i = 0; i < gpCodeTemp.length; i++)
		{
			gpCodeTemp[i] = packet[i + 3];
			end = i + 3;
		}
		_gpCode = new String(gpCodeTemp).trim();
		
		byte[] idTemp = new byte[packet[2]];
		for(int i = 0; i < idTemp.length; i++)
		{
			idTemp[i] = packet[i + end + 1];
		}
		_id = new String(idTemp).trim();
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		setInformation(packet);
		_packetMaxCount = 1;
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
				String usCode = "@" + rs.getString(6);
				
				ResultSet rs2 = db.Query("select * from grouplist where gpcode = " + Server_Code_Manager.codeCutter(_gpCode) + ";");
				rs2.next();
				String gpList = rs2.getString(2);
				StringTokenizer st = new StringTokenizer(gpList, ":");
				String captain = st.nextToken();
				
				String choice;
				if(captain.equals(usCode))
				{
					choice = "TRUE";
				}
				else
				{
					choice = "FALSE";
				}
				
				_activity.send.setPacket(choice.getBytes(), 1024).write();
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
