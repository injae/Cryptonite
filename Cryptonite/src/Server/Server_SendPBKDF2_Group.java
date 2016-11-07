package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server_SendPBKDF2_Group extends Server_Funtion {

	public Server_SendPBKDF2_Group(Server_Client_Activity activity) {
		super(activity);
	}

	Server_DataBase db = Server_DataBase.getInstance();
	String gpCode = null;
	
	@Override
	public void Checker(byte[] packet) {
		_packetMaxCount = 1;
		gpCode = String.valueOf(packet[2]);
		System.out.println("Receive gpCode: " + gpCode);
	}

	@Override
	public void running(int count) throws IOException {
			try {
				Checker(_activity.getReceiveEvent());
				ResultSet rs = db.Query("select * from grouplist where gpcode like '" +gpCode + "';");
				rs.next();
				
				String salt = rs.getString(6);
				int iteration = rs.getInt(5);
				
				_activity.send.setPacket(salt.getBytes(),32).write();
				_activity.send.setPacket(intToByteArray(iteration),4).write();
				
				
			} catch (SQLException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}
			
		}
	
	public  byte[] intToByteArray(int value) {
		byte[] byteArray = new byte[4];
		byteArray[0] = (byte)(value >> 24);
		byteArray[1] = (byte)(value >> 16);
		byteArray[2] = (byte)(value >> 8);
		byteArray[3] = (byte)(value);
		return byteArray;
	}

}
