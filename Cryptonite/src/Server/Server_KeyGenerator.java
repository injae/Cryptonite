package Server;

import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Server_KeyGenerator {
	private byte[] _salt = null;
	private SecretKey _keyData = null;
	private int _iterationCount = 0;
	private SecureRandom _random = null;

	public Server_KeyGenerator()
	{
		init();
	}
	
	public SecretKey getAesKey() { return _keyData; }
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

/* 비밀키와 솔트값과 반복카운트 값을 동시에 생성함
 * 한 아이디에 부여할 때마다 init()을 호출해줘야함
 */
