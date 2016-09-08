package Server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.Cipher;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Function.Function;

public class Server_Get_GroupKey extends Server_Funtion 
{
	int gpcode;
	
	public Server_Get_GroupKey(Server_Client_Activity activity) {
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
		
		Crypto crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, _activity.getKey()));
        ResultSet rs = db.Query("Select *from grouplist where gpcode = "+ gpcode+";");
        try {
			rs.next();
			String groupKey = rs.getString(4);
		 	_activity.send.setPacket(crypto.endecription(groupKey.getBytes())).write();
		 	
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}
}
