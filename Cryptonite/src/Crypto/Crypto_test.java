package Crypto;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Crypto_test {
	public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
		rsaKeyGenerator generator = new rsaKeyGenerator();
		Key _pubKey = generator.get_pubKey();
		Key _priKey = generator.get_priKey();
		aesKeyGenerator uGenerator = new aesKeyGenerator();
		SecretKey secretKey = uGenerator.getAesKey();
		
		byte[] fuck = new Crypto(Crypto_Factory.create("RSA1024", Cipher.ENCRYPT_MODE, _pubKey)).endecription("Fuck".getBytes());
		String fuck2 = new String(new Crypto(Crypto_Factory.create("RSA1024", Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(_priKey.getEncoded())))).endecription(fuck));
		System.out.println(fuck2);
		
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

		Crypto aesEncrypt = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, secretKey));
		Crypto aesDecrypt = new Crypto(
				Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, new SecretKeySpec(decryptData, "AES")));
		byte[] cip = aesEncrypt.endecription(plain);
		byte[] dec = aesDecrypt.endecription(cip);
		System.out.println("decrypted message is : " + new String(dec));
	}
}
