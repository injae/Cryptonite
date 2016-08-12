package Crypto;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class userKeyGenerator {
	private byte[] _salt = null;
	private SecretKey _keyData = null;
	private int _iterationCount = 0;
	private SecureRandom _random = null;

	//Constructor
	public userKeyGenerator()
	{
		init();
	}
	
	//Getter
	public SecretKey getAesKey() { return _keyData; }
	public byte[] getAesKeyBytes() { return _keyData.getEncoded(); }
	public byte[] getSalt() { return _salt; }
	public int getIterationCount() { return _iterationCount; }
	
	public void init(){
		_random = new SecureRandom();
		_keyData = aesKeyGen();
		_salt = saltGen();
		_iterationCount = iCGen(); 
	}
	
	
	//private function
	private SecretKey aesKeyGen(){
		byte[] keyData = new byte[32];
		_random.nextBytes(keyData);
		SecretKey key = new SecretKeySpec(keyData, "AES");
		return key;
	}
	private byte[] saltGen(){
		byte[] salt = new byte[8]; 
		_random.nextBytes(salt);
		return salt;
	}
	private int iCGen(){
		int iterationCount = 1000; //Test Integer
		return iterationCount;
	}
	
}

