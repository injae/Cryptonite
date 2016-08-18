package Crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public  class New_Crypto 
{
	public static final int ENCRYPT = Cipher.ENCRYPT_MODE;
	public static final int DECRYPT = Cipher.DECRYPT_MODE;
	
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
	public long calc(long capacity)
	{
		long remainder = capacity%32;		
		if (remainder != 0) { return capacity - remainder + 32; }
		else 			   { return capacity; }
	}
}