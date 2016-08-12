package Crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class AES256 extends Crypto{
	private static final String algorithm = "AES"; 
	private static final String transformation = algorithm + "/ECB/PKCS5Padding";
	
	public AES256() {
		super();
	}
	public AES256(SecretKey _key) {
		super(_key);
	}
	public AES256(int _mode) {
		super(_mode);
	}
	public AES256(SecretKey _key, int _mode) {
		super(_key, _mode);
	}
	
	public boolean push (byte[] target){
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(_mode, _key);
			_resultData = cipher.doFinal(target);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	public byte[] pop(){
		return _resultData;
	}
}
