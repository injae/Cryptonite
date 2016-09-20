package Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.ResultSet;

import Function.PacketRule;

public class Server_SetGPS extends Server_Funtion implements PacketRule {

	public Server_SetGPS(Server_Client_Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Checker(byte[] packet) {
		_activity.receive.setAllocate(8).setAllocate(8);
		_packetMaxCount = packet[1];
	}

	
	
	
	@Override
	public void running(int count) throws IOException {
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else {
		
			double lat;
			double lng;
			
			Server_DataBase db=Server_DataBase.getInstance();
			
			lat = ByteBuffer.wrap(_activity.receive.getByte()).getDouble();
			lng = ByteBuffer.wrap(_activity.receive.getByte()).getDouble();
			long currentTime = System.currentTimeMillis() + 600000 ; // gps is valid until 10 minutes after
	
			db.Update("update test set lat = '" + lat + "', lng = '" + lng + "', effectiveTime = '" + currentTime + "' where uscode = " + Server_Code_Manager.codeCutter(_activity.getClientCode()) + ";");
			
			Server_Administrator.getInstance().userUpdate("GPS : user - "+_activity.getClientCode() + " lat : "+ lat + " lng : " + lng + "  Updated!");
		}
		
		
		
	}
	
}
