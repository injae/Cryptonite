package Crypto;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class rsaKeyGenerator {
	private Key _pubKey = null;
	private Key _priKey = null;
	
	public rsaKeyGenerator(){
		init();
	}
	
	public void init(){
		KeyPairGenerator generator = null;
		try {
			generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			KeyPair pair = generator.generateKeyPair();
			_pubKey = pair.getPublic();
			_priKey = pair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public Key get_pubKey(){
		return _pubKey;
	}
	public Key get_priKey(){
		return _priKey;
	}
	
	public byte[] get_pubKeybytes(){
		byte[] pubKeyBytes = _pubKey.getEncoded();
		return pubKeyBytes;
	}
	public byte[] get_priKeybytes(){
		byte[] priKeyBytes = _priKey.getEncoded();
		return priKeyBytes;
	}
}
