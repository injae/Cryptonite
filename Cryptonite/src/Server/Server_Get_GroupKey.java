package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Function.Function;

public class Server_Get_GroupKey extends Server_Funtion {
	int gpcode;
	int keynum = 0;

	public Server_Get_GroupKey(Server_Client_Activity activity) {
		super(activity);
	}

	@Override
	public void Checker(byte[] packet) {
		_packetMaxCount = 1;
		keynum = packet[1];
		gpcode = Server_Code_Manager.codeCutter(new String(Function.cuttingByte(2, packet)).trim());
	}

	@Override
	public void running(int count) throws IOException {
		Checker(_activity.getReceiveEvent());
		Server_DataBase db = Server_DataBase.getInstance();

		try {
			if (keynum == 0) {
				ResultSet rs = db.Query("Select *from grouplist where gpcode = " + gpcode + ";");
				rs.next();
				String groupKey = rs.getString(4);

				byte[] groupkey = Base64.getDecoder().decode(groupKey);

				_activity.send.setPacket(groupkey, 32).write();
				
			} else {
				System.out.println("Select groupkey from groupkey where gpcode = '" + gpcode + "' and uscode = '"
						+ Server_Code_Manager.codeCutter(_activity.getClientCode()) + "' and groupkeynum = '" + keynum
						+ "';");
				ResultSet rs = db.Query("Select groupkey from groupkey where gpcode = '" + gpcode + "' and uscode = '"
						+ Server_Code_Manager.codeCutter(_activity.getClientCode()) + "' and groupkeynum = '" + keynum
						+ "';");
				rs.next();
				
				String groupKey = rs.getString(1);
				
				byte[] groupkey = Base64.getDecoder().decode(groupKey);
				
				ResultSet rs1 = db.Query("Select secretkey from test where uscode = " + Server_Code_Manager.codeCutter(_activity.getClientCode()) +";");
				rs1.next();
				String Sk = rs1.getString(1);
				
				byte[] sk = Base64.getDecoder().decode(Sk);
				int len = sk.length;
				
				_activity.send.setPacket(groupkey,128).write();
				_activity.send.setPacket(Function.intToByteArray(len),4).write();
				_activity.send.setPacket(sk,len).write();
				
				
			}

		} catch (SQLException e) {
			// TODO �ڵ� ������ catch ���
			e.printStackTrace();
		}
	}
}
