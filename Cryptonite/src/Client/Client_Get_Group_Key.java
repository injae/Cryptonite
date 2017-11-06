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
	
	public String running(String groupCode)
	{
		Client_Server_Connector csc = Client_Server_Connector.getInstance();
		KeyReposit reposit = KeyReposit.getInstance();
		//Crypto crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, reposit.get_aesKey()));
		
		byte[] event = new byte[1024];
		event[0] = GET_GROUP_KEY;
		Function.frontInsertByte(1, groupCode.getBytes(), event);
		
		System.out.println("get Key");
		
		String GpKey = null;
		try 
		{
			csc.send.setPacket(event).write();
			GpKey = new String(csc.receive.setAllocate(172).read().getByte());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return GpKey;
	}

}
