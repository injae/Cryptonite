package Server;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.userKeyGenerator;

public class Server_KeyExchange extends Server_Funtion {
	private Key _pubKey = null;
	private SecretKey _secretKey = null;

	private Crypto crypto = null;

	public Server_KeyExchange(Server_Client_Activity activity) {
		super(activity);
		// TODO �ڵ� ������ ������ ����
	}

	@Override
	public void Checker(byte[] packet) {
		_activity.receive.setAllocate(162);
		_packetMaxCount = packet[1];
	}

	@Override
	public void running(int count) throws IOException {
		if (count == 1) {
			Checker(_activity.getReceiveEvent());
		} else {
			makeSecretKey();
			KeyFactory keyFactory = null;
			try {
				keyFactory = KeyFactory.getInstance("RSA");
				// Recieve public key from client
				byte[] pubKeyBytes = _activity.receive.getByte();
				PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(pubKeyBytes));
				
				// Encrypt secret key by public key
				crypto = new Crypto(Crypto_Factory.create("RSA1024", Cipher.ENCRYPT_MODE, pubKey));
				byte[] aesKey = crypto.endecription(_secretKey.getEncoded());
				
				// Send encrypted secret key to client
				_activity.send.setPacket(aesKey, 128);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public SecretKey get_secretKey() {
		return _secretKey;
	}

	private void makeSecretKey() {
		userKeyGenerator generator = new userKeyGenerator();

		this._secretKey = generator.getAesKey();
	}
}
