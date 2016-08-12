package Crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class RSA1024 extends Crypto{
	private static final String algorithm = "RSA"; 
	private static final String transformation = algorithm + "/None/PKCS1Padding";
	
	//Constructor
	public RSA1024() {
		super();
	}
	public RSA1024(SecretKey _key) {
		super(_key);
	}
	public RSA1024(int _mode) {
		super(_mode);
	}
	public RSA1024(SecretKey _key, int _mode) {
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
