package Crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public  class Crypto 
{	
	private Cipher _cipher;

	public Crypto(Cipher cipher) { _cipher = cipher; }
	
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
	public long calc(long capacity)
	{
		long remainder = capacity%32;		
		if (remainder != 0) { return capacity - remainder + 32; }
		else 			   { return capacity; }
	}
}