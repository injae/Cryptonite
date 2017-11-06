package Client;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.KeyReposit;
import Function.Function;

import java.io.IOException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Function.Function;
import Function.PacketRule;

public class Client_Get_Group_priKey implements PacketRule{

	
	
	public PrivateKey running(String groupCode)
	{
		Client_Server_Connector csc = Client_Server_Connector.getInstance();
		KeyReposit reposit = KeyReposit.getInstance();
		System.out.println(Base64.getEncoder().encodeToString(reposit.get_aesKey().getEncoded()));
		//Crypto crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, reposit.get_aesKey()));
		
		byte[] event = new byte[1024];
		event[0] = GET_GROUP_PRIKEY;
		Function.frontInsertByte(1, groupCode.getBytes(), event);
		
		System.out.println("get Key");
		
		PrivateKey GpKey = null;
		try 
		{
			csc.send.setPacket(event).write();
			int len = Function.byteArrayToInt(csc.receive.setAllocate(4).read().getByte());
			System.out.println("zzzzz" + len);
			GpKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(csc.receive.setAllocate(len).read().getByte()));
			System.out.println("recevie pri");
		}
		catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		return GpKey;
	}
}
