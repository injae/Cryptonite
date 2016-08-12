package Crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public abstract class Crypto {
	public static final int ENCRYPT = Cipher.ENCRYPT_MODE;
	public static final int DECRYPT = Cipher.DECRYPT_MODE;
	
	protected byte[] _resultData = null;
	
	protected SecretKey _key = null;
	protected int _mode = 0;

	// Constructor
	public Crypto() {
	}

	public Crypto(SecretKey _key) {
		this._key = _key;
	}

	public Crypto(int _mode) {
		this._mode = _mode;
	}

	public Crypto(SecretKey _key, int _mode) {
		this._key = _key;
		this._mode = _mode;
	}
	
	//Setter
	public void set_key(SecretKey _key) {
		this._key = _key;
	}
	public void set_mode(int _mode){
		this._mode = _mode;
	}
	
	//Calculate capacity of file after encrypting
	public static int calc(int capacity){
		int reminder = capacity%32;
		if (reminder != 0){
			return capacity-reminder+32;
		} else return capacity;
	}
	
	//Execute Function
	public boolean push (byte[] target){
		return false;
	}
	public byte[] pop (){
		return null;
	}
}