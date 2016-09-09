package Client;

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

public class Client_Get_Group_Key implements PacketRule
{

	public Client_Get_Group_Key() 
	{

	}
	
	public SecretKey running(String groupCode)
	{
		Client_Server_Connector csc = Client_Server_Connector.getInstance();
		KeyReposit reposit = KeyReposit.getInstance();
		System.out.println(Base64.getEncoder().encodeToString(reposit.get_aesKey().getEncoded()));
		Crypto crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, reposit.get_aesKey()));
		
		byte[] event = new byte[1024];
		event[0] = GET_GROUP_KEY;
		Function.frontInsertByte(1, groupCode.getBytes(), event);
		
		SecretKey GpKey = null;
		try 
		{
			csc.send.setPacket(event).write();
			GpKey = new SecretKeySpec(crypto.endecription(csc.receive.setAllocate(32).read().getByte()), "AES");
		} catch (IOException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
		return GpKey;
	}

}
