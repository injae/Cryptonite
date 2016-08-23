//Not already done test module
package Client;

import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.rsaKeyGenerator;

public class Client_KeyExchange {
	private Client_Server_Connector csc = null;
	private Crypto crypto = null;

	private Key _priKey = null;
	private Key _pubKey = null;

	private SecretKey _secretKey = null;

	public Client_KeyExchange() {
		byte[] encryptData = null;

		// Server Connect
		csc = Client_Server_Connector.getInstance();

		setKey();
		crypto = new Crypto(Crypto_Factory.create("RSA1024", Cipher.DECRYPT_MODE, _priKey));

		// Send public key to server
		try {
			csc.send.setPacket(_pubKey.getEncoded()).write();

			// Recieve encrypted secret Key from server
			encryptData = csc.receive.read().getByte();

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
