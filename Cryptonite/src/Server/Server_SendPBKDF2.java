package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server_SendPBKDF2 extends Server_Funtion {

	public Server_SendPBKDF2(Server_Client_Activity activity) {
		super(activity);
	}

	Server_DataBase db = Server_DataBase.getInstance();
	
	@Override
	public void Checker(byte[] packet) {
		_packetMaxCount = 1;
		System.out.println("00000");

	}

	@Override
	public void running(int count) throws IOException {
			try {
				Checker(_activity.getReceiveEvent());
				System.out.println("22222");
				ResultSet rs = db.Query("select * from test where uscode like '" +Server_Code_Manager.codeCutter(_activity.getClientCode()) + "';");
				rs.next();
				
				System.out.println("11111");
				
				String salt = rs.getString(8);
				int iteration = rs.getInt(9);
				
				System.out.println("Send : " + salt + iteration);
				System.out.println(salt.getBytes().length);
				_activity.send.setPacket(salt.getBytes(),16).write();
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
