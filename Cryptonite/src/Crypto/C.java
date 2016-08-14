package Crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public  class C 
{
	protected Cipher _cipher;
	protected byte[] _resultData = null;

	public C(Cipher cipher)
	{
		_cipher = cipher;
	}
	
	//Execute Function
	public void push (byte[] target)
	{
		try
		{
			_resultData = _cipher.doFinal(target);
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e) 
		{
			e.printStackTrace();
		}
	}
	
	public byte[] pop()
	{
		return _resultData;
	}
	
	//Calculate capacity of file after encrypting
	public static int calc(int capacity)
	{
		int reminder = capacity%32;		
		if (reminder != 0) { return capacity - reminder + 32; }
		else 			   { return capacity; }
	}
}