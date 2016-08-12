package Function;

import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyGenerator {
	private byte[] _salt = null;
	private SecretKey _keyData = null;
	private int _iterationCount = 0;
	private SecureRandom _random = null;

	//Constructor
	public KeyGenerator()
	{
		init();
	}
	
	//Getter
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

/* 鍮꾨��궎�� �넄�듃媛믨낵 諛섎났移댁슫�듃 媛믪쓣 �룞�떆�뿉 �깮�꽦�븿
 * �븳 �븘�씠�뵒�뿉 遺��뿬�븷 �븣留덈떎 init()�쓣 �샇異쒗빐以섏빞�븿
 */
