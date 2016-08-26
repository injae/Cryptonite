package Crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Crypto_Factory
{
    public final static String AES256 = "AES256";
    public final static String RSA1024 = "RSA1024";

    public static Cipher create(String type, int mode, Key key)
    {
        Cipher cipher = null;
        try
        {
            switch(type)
            {
                case "AES256":
                    cipher = Cipher.getInstance("AES/CTR/NoPadding");
                    IvParameterSpec iv = new IvParameterSpec("1234567890123456".getBytes());
                    cipher.init(mode, (SecretKey)key, iv);
                    break;
                case "RSA1024":
                    cipher = Cipher.getInstance("RSA");
                    if(mode == Cipher.ENCRYPT_MODE)	cipher.init(mode, (PublicKey)key);
                    else cipher.init(mode, (PrivateKey) key);
                    break;
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

        return cipher;
    }
}
