package Crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Crypto_Factory 
{
	public final static String AES256 = "AES256";
	public final static String RSA1024 = "RSA1024";
	
	public static Cipher create(String type, int mode, SecretKey key)
	{			
		Cipher cipher = null;
		try 
		{
			switch(type)
			{
			case "AES256":
				cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); break;
			case "RSA1024":
				cipher = Cipher.getInstance("RSA/None/PKCS1Padding"); break;
			}
			cipher.init(mode, key);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		
		return cipher;
	}
}
