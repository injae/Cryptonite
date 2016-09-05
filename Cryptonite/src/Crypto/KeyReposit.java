package Crypto;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyReposit {
	private static KeyReposit _singleton = null;
	
	private int _level = 1;

	private SecretKey _aesKey_lv1 = null; // Default AES Key
	private SecretKey _aesKey_lv2 = null; // AES Key based on MAC address
	private SecretKey _aesKey_lv3 = null; // AES Key based on physical address
											// using GPS sensors
	private SecretKey _rsaKey = null; // AES Key for comunication

	private KeyReposit() {
	};
	
	public static KeyReposit getInstance() {
		if (_singleton == null) {
			_singleton = new KeyReposit();
		}
		return _singleton;
	}
	
	public void set_level(int level){
		this._level = level;
	}

	public void set_aesKey(SecretKey aesKey) {
		this._aesKey_lv1 = aesKey;
		makeKey();
	}

	public void set_rsaKey(SecretKey rsaKey) {
		this._rsaKey = rsaKey;
	}

	public SecretKey get_aesKey() {
		SecretKey aesKey = null;
		switch (_level) {
		case 1:
			aesKey = _aesKey_lv1;
			break;
		case 2:
			aesKey = _aesKey_lv2;
			break;
		case 3:
			aesKey = _aesKey_lv3;
			break;
		}
		return aesKey;
	}

	public SecretKey get_rsaKey() {
		return _rsaKey;
	}
	
	public String get_fileExtention() {
		String fileExtension = null;
		
		switch (_level) {
		case 1:
			fileExtension = ".cnec";
			break;
		case 2:
			fileExtension = ".cnmc";
			break;
		case 3:
			fileExtension = ".cnac";
			break;
		}
		return fileExtension;
	}

	private void makeKey() {
		
		NetworkInterface ni = null;
		byte[] originalAesKeyBytes = _aesKey_lv1.getEncoded();
		byte[] xorAesKeyBytes = new byte[originalAesKeyBytes.length];
		
		
		try {
			// create lv2 key
			ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] mac = ni.getHardwareAddress();
			
			for(int i =0; i<originalAesKeyBytes.length ; i++){
				if(i < mac.length){
					xorAesKeyBytes[i] = (byte) (originalAesKeyBytes[i] ^ mac[i]);
				} else {
					xorAesKeyBytes[i] = originalAesKeyBytes[i];
				}
			}
			
			_aesKey_lv2 = new SecretKeySpec(xorAesKeyBytes, "AES");
			
			// create lv3 key
			
			_aesKey_lv3 = _aesKey_lv1; //test
			
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
