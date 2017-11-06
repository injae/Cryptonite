package Server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import Function.Function;

public class Server_Get_GroupPriKey extends Server_Funtion {

	int gpcode;
		
		public Server_Get_GroupPriKey(Server_Client_Activity activity) {
			super(activity);
		}
	
		@Override
		public void Checker(byte[] packet) 
		{
			_packetMaxCount = 1;
			gpcode = Server_Code_Manager.codeCutter(new String(Function.cuttingByte(1, packet)).trim());
		}
	
		@Override
		public void running(int count) throws IOException 
		{
			Checker(_activity.getReceiveEvent());
			Server_DataBase db = Server_DataBase.getInstance();
			byte[] len = null;
			
			 try {
	
	        ResultSet rs = db.Query("Select *from grouplist where gpcode = "+ gpcode+";");
			rs.next();
			String groupKey = rs.getString(12);
			byte[] prikey = Base64.getDecoder().decode(groupKey);

			len = Function.intToByteArray(prikey.length);
			_activity.send.setPacket(len,4).write();
			System.out.println("send :" + Function.byteArrayToInt(len));
		 	_activity.send.setPacket(prikey,prikey.length).write();
		 	
			} catch (SQLException e) {
				// TODO �ڵ� ������ catch ���
				e.printStackTrace();
			}
		}
}
