package Crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public  class New_Crypto 
{
	private Cipher _cipher;

	public New_Crypto(Cipher cipher) { _cipher = cipher; }
	
	//Execute Function
	public byte[] endecription (byte[] target)
	{
		try
		{
			return _cipher.doFinal(target);
			
		}catch (IllegalBlockSizeException e){
			e.printStackTrace();
		}catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//Calculate capacity of file after encrypting
	public static int calc(int capacity)
	{
		int reminder = capacity%32;		
		if (reminder != 0) { return capacity - reminder + 32; }
		else 			   { return capacity; }
	}
}