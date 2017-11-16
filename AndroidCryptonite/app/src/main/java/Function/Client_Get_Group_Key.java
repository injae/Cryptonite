package Function;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;

public class Client_Get_Group_Key implements PacketRule
{

    public SecretKey running(String groupCode, int keynum) throws NoSuchAlgorithmException //keynum 0 = fileupload : 그냥 그룹aeskey 리턴	//non-zero = filedownload : 해당 keynum의 key
    {
        Client_Server_Connector csc = Client_Server_Connector.getInstance();
        KeyReposit reposit = KeyReposit.getInstance();
        //Crypto crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, reposit.get_aesKey()));

        byte[] event = new byte[1024];
        event[0] = GET_GROUP_KEY;
        event[1] = (byte) keynum;

        Function.frontInsertByte(2, groupCode.getBytes(), event);

        System.out.println("get Key");

        byte[] GpKey = null;
        int sklen = 0;
        byte[] sk = null;
        SecretKey ret = null;
        try
        {
            csc.send.setPacket(event).write();
            if (keynum !=0)												//download
            {
                GpKey = csc.receive.setAllocate(128).read().getByte();

                sklen = Function.byteArrayToInt(csc.receive.setAllocate(4).read().getByte());
                sk = csc.receive.setAllocate(sklen).read().getByte();

                EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(sk);
                KeyFactory generator = KeyFactory.getInstance("RSA");
                PrivateKey privateKey = generator.generatePrivate(privateKeySpec);

                byte[] t = Crypto_Factory.create("RSA1024", Cipher.DECRYPT_MODE, privateKey).doFinal(GpKey);

                ret = new SecretKeySpec(t,"AES");
            }
            else														//upload
            {
                ret = new SecretKeySpec(csc.receive.setAllocate(32).read().getByte(),"AES");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }

}
