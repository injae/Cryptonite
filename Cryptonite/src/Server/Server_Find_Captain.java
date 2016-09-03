package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Server_Find_Captain extends Server_Funtion
{
	// Instance
	private String _gpCode;
	private String _gpName;
	private String _id;
	private boolean _passCheck;
	
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
		_gpName = new String(gpCodeTemp).trim();
		
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
				_passCheck = false;	
				
				Server_DataBase db = Server_DataBase.getInstance();
				ResultSet rs = db.Query("select * from test where id = '" + _id + "';");
				rs.next();
				String usCode = "@" + rs.getString(6);
				
				ResultSet rs2 = db.Query("select * from grouplist where gpname like '%" + _gpName + "%';");
				String choice = "DEFAULT";
				int counter = 0;
				while(rs2.next())
				{
					_gpCode = "$" + rs2.getString(1);
					String gpList = rs2.getString(2);
					StringTokenizer st = new StringTokenizer(gpList, ":");
					while(st.hasMoreTokens())
					{
						String temp = st.nextToken();
						if(temp.equals(usCode))
						{
							if(counter == 0)
							{
								choice = "TRUE:";
							}
							_passCheck = true;
							break;
						}
						counter++;
					}
					if(_passCheck)
					{
						break;
					}
				}
				
				if(!choice.equals("TRUE:"))
				{
					choice = "FALSE:";
				}
				choice += _gpCode;
				
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
