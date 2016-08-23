package Crypto;

import javax.crypto.SecretKey;

public class KeyReposit {
	private static KeyReposit _singleton = null;
	
	private SecretKey _aesKey = null;
	private SecretKey _rsaKey = null; //AES Key for comunication
	
	private KeyReposit() { };
	
	public static KeyReposit getInstance() {
		if (_singleton == null) 
		{ 
			_singleton = new KeyReposit(); 
		}
		return _singleton;
	}
	
	public void set_aesKey(SecretKey aesKey) { this._aesKey = aesKey; }
	public void set_rsaKey(SecretKey rsaKey) { this._rsaKey = rsaKey; }
	
	public SecretKey get_aesKey() { return _aesKey; }
	public SecretKey get_rsaKey() {return _rsaKey; }	
}
