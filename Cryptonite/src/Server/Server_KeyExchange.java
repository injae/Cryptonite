package Server;

import java.io.IOException;
import java.security.Key;

import javax.crypto.SecretKey;

import Crypto.userKeyGenerator;

public class Server_KeyExchange extends Server_Funtion
{
	private Key _pubKey = null;
	
	private SecretKey _secretKey = null;
	
	
	public Server_KeyExchange(Server_Client_Activity activity) {
		super(activity);
		// TODO 磊悼 积己等 积己磊 胶庞
	}

	@Override
	public void Checker(byte[] packet)
	{
		_packetMaxCount = packet[1];
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			byte[] pubKey = _activity.receive.getByte();
			
			byte[] aesKey = null;
			_activity.send.setPacket(aesKey);
		}
	}
	
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
