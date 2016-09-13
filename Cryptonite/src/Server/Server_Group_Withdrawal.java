package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import Function.Function;

public class Server_Group_Withdrawal extends Server_Funtion {

	String _gpcode;
	
	public Server_Group_Withdrawal(Server_Client_Activity activity) {
		super(activity);
	}

	@Override
	public void Checker(byte[] packet) {
		_packetMaxCount = 1;
		_gpcode = new String(Function.cuttingByte(1, packet)).trim();
	}

	@Override
	public void running(int count) throws IOException {
		try {
			Checker(_activity.getReceiveEvent());
			Server_DataBase db = Server_DataBase.getInstance();
			
			ResultSet rs = db.Query("select *from grouplist where gpcode = " + Server_Code_Manager.codeCutter(_gpcode) + ";");		
			rs.next();	
			
			StringTokenizer gplist = new StringTokenizer(rs.getString(2), ":");
			String save = "";
			while(gplist.hasMoreTokens())
			{
				String temp = gplist.nextToken();
				if(!temp.equals(_activity.getClientCode()))
				{
					if(save.length() != 0) {save += ":" + temp;}
					else 				   {save = temp; }
				}
			}
			
			if(save.length() == 0)
			{
				db.Update("delete from grouplist where gpcode = "+Server_Code_Manager.codeCutter(_gpcode)+";"); 
			}
			else 
			{
				db.Update("update grouplist set gplist = '" + save + "' where gpcode = " + Server_Code_Manager.codeCutter(_gpcode) + ";");
			}
			
			ResultSet rs3 = db.Query("select * from test where uscode = '" + Server_Code_Manager.codeCutter(_activity.getClientCode()) + "';");
			rs3.next();
			String mygrouplist = rs3.getString(10);
			gplist = new StringTokenizer(mygrouplist, ":");
			save = "";
			while(gplist.hasMoreTokens())
			{
				String temp = gplist.nextToken();
				if(!temp.equals(_gpcode))
				{
					if(save.length() != 0) { save += ":" + temp; }
					else 				   { save = temp; }
				}	
			}
			if(save.length() == 0) { save = "NULL"; }
			db.Update("update test set mygrouplist = '" + save + "' where uscode = " + Server_Code_Manager.codeCutter(_activity.getClientCode()) + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
