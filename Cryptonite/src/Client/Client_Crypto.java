package Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class Client_Crypto {
	SecretKey _key = null;
	int _mode = 0;
	
	//Constructor
	public Client_Crypto ()	{}
	public Client_Crypto (SecretKey _key) {
		this._key = _key;
	}
	public Client_Crypto (int _mode){
		this._mode = _mode;
	}
	public Client_Crypto (SecretKey _key, int _mode){
		this._key = _key;
		this._mode = _mode;
	}
	
	//상속클래스
	public class Client_FileCrypt{
		
	}
	public class Client_PwCrypt{
		
	}
	
	//Encrypting Core
	private class Client_AES256{
		static final String algorithm = "AES"; 
		private static final String transformation = algorithm + "/ECB/PKCS5Padding";
		
		File _source = null;
		File _dest = null;
		
		Cipher cipher = Cipher.getInstance(transformation);
		//ciper.init(_mode,_key);
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		
		try {
			input = new BufferedInputStream(new FileInputStream(_source));
			output = new BufferedOutputStream(new FileOutputStream(_dest));
			byte[] buffer = new byte[1024];
			int read = -1;
			while ((read = input.read(buffer)) != -1) {
				output.write(cipher.update(buffer, 0, read));
			}
			output.write(cipher.doFinal());
		} finally {
			if (output != null) {
				try { output.close(); } catch(IOException ie) {}
			}
			if (input != null) {
				try { input.close(); } catch(IOException ie) {}
			}
		}
	}
}


