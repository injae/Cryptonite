//Not already done test module
package Client;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.rsaKeyGenerator;
import Crypto.userKeyGenerator;
import Function.PacketRule;

public class Client_KeyExchange implements PacketRule {

/*	for test
 * public static void main(String[] args) {
		rsaKeyGenerator generator = new rsaKeyGenerator();
		Key _pubKey = generator.get_pubKey();
		Key _priKey = generator.get_priKey();
		userKeyGenerator uGenerator = new userKeyGenerator();
		SecretKey secretKey = uGenerator.getAesKey();

		Crypto encrypt = new Crypto(Crypto_Factory.create("RSA1024", Cipher.ENCRYPT_MODE, _pubKey));
		Crypto decrypt = new Crypto(Crypto_Factory.create("RSA1024", Cipher.DECRYPT_MODE, _priKey));
		
		byte[] plain = "message for test".getBytes();
		
		byte[] pubKeyBytes = _pubKey.getEncoded();
		System.out.println("public key size(byte) : " + pubKeyBytes.length);
		byte[] secretKeyBytes = secretKey.getEncoded();
		System.out.println("secret Key size(byte) : " + secretKeyBytes.length);
		byte[] encryptData = encrypt.endecription(secretKeyBytes);
		System.out.println("encrypted Key size(byte) : " + encryptData.length);
		byte[] decryptData = decrypt.endecription(encryptData);
		System.out.println("decrypted Key size(byte) : " + decryptData.length);
		
		Crypto aesEncrypt = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE , secretKey));
		Crypto aesDecrypt = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, new SecretKeySpec(decryptData, "AES")));
		byte[] cip = aesEncrypt.endecription(plain);
		byte[] dec = aesDecrypt.endecription(cip);
		System.out.println("decrypted message is : " + new String(dec));
	}*/

	private Client_Server_Connector csc = null;
	private Crypto crypto = null;

	private Key _priKey = null;
	private Key _pubKey = null;

	private SecretKey _secretKey = null;

	public Client_KeyExchange() {
		// Server Connect
		csc = Client_Server_Connector.getInstance();

		setKey();
		crypto = new Crypto(Crypto_Factory.create("RSA1024", Cipher.DECRYPT_MODE, _priKey));

		// Send public key to server
		try {
			byte[] event = new byte[1024];
			event[0] = KEY_EXCHANGE;
			event[1] = 2;
			csc.send.setPacket(event).write();

			csc.send.setPacket(_pubKey.getEncoded(), 162).write();

			byte[] encryptData = csc.receive.setAllocate(128).read().getByte();

			// Decrypt recieved secret Key from server
			this._secretKey = new SecretKeySpec(crypto.endecription(encryptData), "AES");
		}
		// exchange done
		catch (IOException e) {
			System.out.println("key exchange failed");
			e.printStackTrace();
		}
	}

	public SecretKey get_secretKey() {
		return _secretKey;
	}

	private void setKey() {
		rsaKeyGenerator generator = new rsaKeyGenerator();
		this._priKey = (PrivateKey) generator.get_priKey();
		this._pubKey = generator.get_pubKey();
	}

}
