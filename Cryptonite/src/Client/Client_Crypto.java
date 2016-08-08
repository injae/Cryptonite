package Client;

import javax.crypto.*;

public class Client_Crypto {
	public static final int _encrypt = Cipher.ENCRYPT_MODE;
	public static final int _decrypt = Cipher.DECRYPT_MODE;
	
	private byte[] _resultData = null;
	
	private SecretKey _key = null;
	private int _mode = 0;

	// Constructor
	public Client_Crypto() {
	}

	public Client_Crypto(SecretKey _key) {
		this._key = _key;
	}

	public Client_Crypto(int _mode) {
		this._mode = _mode;
	}

	public Client_Crypto(SecretKey _key, int _mode) {
		this._key = _key;
		this._mode = _mode;
	}
	
	public static int calc(int capacity){
		int reminder = capacity%32;
		if (reminder != 0){
			return capacity-reminder+32;
		} else return capacity;
	}
	
	public boolean push (byte[] target){
		return false;
	}
	public byte[] pop (){
		return null;
	}
	
	// Encrypting Core
	public class Client_AES256 {
		private static final String algorithm = "AES"; 
		private static final String transformation = algorithm + "/ECB/PKCS5Padding";
		
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
	
	public class Client_RSA1024 {
		
	}
}
