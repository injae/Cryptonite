package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class Server_Delete_Group extends Server_Funtion 
{	
	// Instance
	private String gpCode;
	
	// Constructors
	public Server_Delete_Group(Server_Client_Activity activity) 
	{
		super(activity);
	}

	// Methods
	private void setGroupCode(byte[] packet)
	{
		byte[] temp = new byte[300];
		for(int i = 0; i < temp.length; i++)
		{
			temp[i] = packet[i + 1];
		}
		gpCode = new String(temp).trim();
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		setGroupCode(packet);
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
				ResultSet rs = db.Query("select *from grouplist where gpcode = " + Integer.parseInt(gpCode.substring(1)) + ";");
				rs.next();
				StringTokenizer st = new StringTokenizer(rs.getString(2), ":");
				while(st.hasMoreTokens())
				{
					int uscode = Integer.parseInt(st.nextToken().substring(1));
					ResultSet rs2 = db.Query("select *from test where uscode = " + uscode + ";");
					rs2.next();
					StringTokenizer st2 = new StringTokenizer(rs2.getString(10), ":");
					String save = "";
					for(int i = 0; i < st2.countTokens(); i++)
					{
						String temp = st2.nextToken();
						if(!temp.equals(gpCode))
						{
							save += temp;
							if(i != st2.countTokens() - 1)
							{
								save += ":";
							}
						}
					}
					if(save.length() == 0)
					{
						save = "NULL";
					}
					
					System.out.println(save);
					db.Update("update test set mygrouplist = '" + save + "' where uscode = " + uscode + ";");
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