//Not already done test module
package Client;

import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Crypto.rsaKeyGenerator;
import Function.PacketRule;

public class Client_KeyExchange implements PacketRule {



    private Client_Server_Connector csc = null;
    private Crypto crypto = null;

    private Key _priKey = null;
    private Key _pubKey = null;

    private SecretKey _secretKey = null;

    public Client_KeyExchange() {
        // Server Connect
        csc = Client_Server_Connector.getInstance();
       
        setKey();
        crypto = new Crypto(Crypto_Factory.create("RSA1024", Cipher.DECRYPT_MODE, _priKey));

        // Send public key to server
        try {
            byte[] event = new byte[1024];
            event[0] = KEY_EXCHANGE;
            event[1] = 2;
            csc.send.setPacket(event).write();
    
            csc.send.setPacket(_pubKey.getEncoded(), 162).write();
            byte[] encryptData = csc.receive.setAllocate(128).read().getByte();

            // Decrypt recieved secret Key from server
            this._secretKey = new SecretKeySpec(crypto.endecription(encryptData), "AES");
            KeyReposit reposit = KeyReposit.getInstance();
            reposit.set_rsaKey(this._secretKey);
        
        }
       
        catch (IOException e) {
            System.out.println("key exchange failed");
            e.printStackTrace();
        }
    }

    private void setKey() {
        rsaKeyGenerator generator = new rsaKeyGenerator();
        this._priKey = (PrivateKey) generator.get_priKey();
        this._pubKey = generator.get_pubKey();
    }

}
