package Client;

import java.io.IOException;

import javax.crypto.Cipher;

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
		Crypto crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, reposit.get_aesKey()));
		
		byte[] event = new byte[1024];
		event[0] = GET_GROUP_KEY;
		Function.frontInsertByte(1, groupCode.getBytes(), event);
		csc.send.setPacket(event);
		String GpKey = null;
		try 
		{
			GpKey = new String(crypto.endecription(csc.receive.setAllocate(128).read().getByte()));
		} catch (IOException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
		return GpKey;
	}

}
