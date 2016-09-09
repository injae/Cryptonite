package Server;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Crypto.aesKeyGenerator;

public class Server_KeyExchange extends Server_Funtion {
    private SecretKey _secretKey = null;

    private Crypto crypto = null;

    public Server_KeyExchange(Server_Client_Activity activity) {
        super(activity);
        // TODO �ڵ� ������ ������ ����
    }

    @Override
    public void Checker(byte[] packet) {
        _activity.receive.setAllocate(162);
        _packetMaxCount = packet[1];
    }

    @Override
    public void running(int count) throws IOException {
        if (count == 1) 
        {
            Checker(_activity.getReceiveEvent());
        }
        else 
        {
            makeSecretKey();

            try {
                // Recieve public key from client
            	
                byte[] pubKeyBytes = _activity.receive.getByte();
                PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKeyBytes));
                
                // Encrypt secret key by public key
                crypto = new Crypto(Crypto_Factory.create("RSA1024", Cipher.ENCRYPT_MODE, pubKey));
                byte[] aesKey = crypto.endecription(_secretKey.getEncoded());
                
                // Send encrypted secret key to client
                _activity.send.setPacket(aesKey, 128).write();
                
                _activity.send.init(_activity.getKey());
            	_activity.receive.init(_activity.getKey());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void makeSecretKey() {
        aesKeyGenerator generator = new aesKeyGenerator();

        this._secretKey = generator.getAesKey();
        
        _activity.setKey(_secretKey);
    }
}
