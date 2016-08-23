package Server;

import java.security.Key;

import javax.crypto.SecretKey;

import Crypto.userKeyGenerator;

public class Server_KeyExchange {
	private Key _pubKey = null;
	
	private SecretKey _secretKey = null;
	
	public void exchange(){
		makeSecretKey();
		//Client Connect
		//Recieve public key from client
		//Encrypt secret key by public key
		//Send encrypted secret key to client 
		//exchange done
	}
	public SecretKey get_secretKey(){
		return _secretKey;
	}
	
	private void makeSecretKey() {
		userKeyGenerator generator = new userKeyGenerator();
		
		this._secretKey = generator.getAesKey();
	}
}
