package Server;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.Cipher;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Function.Function;

public class Server_GetGPS extends Server_Funtion {

	private double _xpos = 0;
	private double _ypos = 0;

	public Server_GetGPS(Server_Client_Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	Server_DataBase db = Server_DataBase.getInstance();
	String id = null; // need to where get id

	@Override
	public void Checker(byte[] packet) {
		// TODO Auto-generated method stub
		_packetMaxCount = packet[1];
	}

	@Override
	public void running(int count) throws IOException {
		// TODO Auto-generated method stub
		if (count == 1) {
			Checker(_activity.getReceiveEvent());
		} else {
			try {
				ResultSet rs = db.Query("select * from test where id like '" + id + "';");

				_xpos = rs.getDouble(11);
				_ypos = rs.getDouble(12);
				
				_activity.send.setPacket(Function.doubleToByteArray(_xpos));
				_activity.send.setPacket(Function.doubleToByteArray(_ypos));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
