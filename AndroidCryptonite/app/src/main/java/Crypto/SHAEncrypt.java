package Crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Crypto.Base64Coder;

/**
 * Created by olleh on 2016-08-19.
 */
public class SHAEncrypt {

    private static MessageDigest _messageDigest;
    private static byte[] _temp;

    public static String SHAEncrypt(String str)
    {
        if(_messageDigest==null)
            try {
                _messageDigest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        _temp = str.getBytes();
        _messageDigest.update(_temp);

        return new String(Base64Coder.encode(_messageDigest.digest()));
    }

}
