package Crypto;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

public class rsaKeyGenerator
{
	private PublicKey _pubKey = null;
	private PrivateKey _priKey = null;
	
	public rsaKeyGenerator(){
		init();
	}
	
	public void init(){
		KeyPairGenerator generator = null;
		try {
			generator = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			generator.initialize(1024,random);
			KeyPair pair = generator.generateKeyPair();
			_pubKey = pair.getPublic();
			_priKey = pair.getPrivate();
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
	}
	
	public Key get_pubKey(){
		return _pubKey;
	}
	public Key get_priKey(){
		return _priKey;
	}
	
	public String get_PubKeyToString(){
		return Base64.getEncoder().encodeToString(get_pubKeybytes());
	}
	
	public String get_PriKeyToString(){
		return Base64.getEncoder().encodeToString(get_priKeybytes());
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
