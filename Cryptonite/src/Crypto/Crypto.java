package Crypto;

import java.nio.ByteBuffer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class Crypto {
	private Cipher _cipher;

	public Crypto(Cipher cipher) {
		init(cipher);
	}

	public void init(Cipher cipher) {
		_cipher = cipher;
	}
	
	// Execute Function
	public byte[] endecription(byte[] target) {
		try {
			return _cipher.doFinal(target);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ByteBuffer endecription(ByteBuffer target) {
        try {
            ByteBuffer bb = ByteBuffer.allocate(target.limit());
            bb.put(_cipher.doFinal(target.array())); bb.flip();
            return bb;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

	// Calculate capacity of file after encrypting
	public static int calc(int capacity) {
		int remainder = capacity % 32;
		System.out.println("capacity : " + capacity);
		System.out.println("remainder : " + remainder);
		if (remainder != 0) {
			System.out.println(capacity - remainder + 32);
			return capacity - remainder + 32;
		} else {
			System.out.println(capacity);
			return capacity;
		}
	}
}