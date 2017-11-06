package Server;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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
		
		//byte[] groupkey = Base64.getDecoder().decode(groupKey.getBytes());
		//System.out.println(new Crypto(Crypto_Factory.create("RSA1024", Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey.getBytes()))))).endecription(groupkey).length);
		
		//System.out.println("send : " + groupkey.length);
	 	_activity.send.setPacket(groupKey.getBytes(),172).write();
	 	
		} catch (SQLException e) {
			// TODO �ڵ� ������ catch ���
			e.printStackTrace();
		}
	}
}
