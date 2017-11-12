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
	
		
		 try {

        ResultSet rs = db.Query("Select *from grouplist where gpcode = "+ gpcode+";");
		rs.next();
		String groupKey = rs.getString(4);
		
		byte[] groupkey = Base64.getDecoder().decode(groupKey);
		
	 	_activity.send.setPacket(groupkey, 32).write();
	 	
		} catch (SQLException e) {
			// TODO �ڵ� ������ catch ���
			e.printStackTrace();
		}
	}
}
