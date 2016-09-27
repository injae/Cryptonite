package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class Server_Check_GPS {

	
	public boolean check(String filename, Server_Client_Activity _activity)
	{
		try {
			
			Server_DataBase db = Server_DataBase.getInstance();
			StringTokenizer st = new StringTokenizer(filename, "\\");
			st.nextToken();	st.nextToken();
			
			int gpcode = Server_Code_Manager.codeCutter(st.nextToken());	//gpcode
			ResultSet rs = db.Query("select * from grouplist where gpcode like '" + gpcode + "';");
			rs.next();
			boolean usegps = rs.getBoolean(7);	//check group uses gps
			double gplat = rs.getDouble(8);
			double gplng = rs.getDouble(9);
			double gpradius = rs.getDouble(10);
			
			if (usegps)
			{
				ResultSet rs1 = db.Query("select * from test where uscode = '"+ Server_Code_Manager.codeCutter(_activity.getClientCode()) + "';");
				rs1.next();
				
				double lat = rs1.getDouble(11);
				double lng = rs1.getDouble(12);
				long effectiveTime = rs1.getLong(13);
				
				if(System.currentTimeMillis() < effectiveTime)	//less than 10 minutes after the last registration gps.
				{
					double distance = Server_Code_Manager.getInstance().distance(lat, lng, gplat, gplng);
					
					if (distance > gpradius)	// Out of the range
					{
						_activity.send.setPacket("distance".getBytes(),500).write();
						return false;
					}
				}
				else 
				{
					_activity.send.setPacket("timeover".getBytes(),500).write();
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
