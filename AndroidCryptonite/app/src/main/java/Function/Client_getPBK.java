package Function;

import android.util.Base64;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import static Function.Function.byteArrayToInt;

/**
 * Created by 전용범 on 2017-11-30.
 */

public class Client_getPBK implements PacketRule{

     Client_Server_Connector _csc;

     public Client_getPBK(){
         _csc = Client_Server_Connector.getInstance();
     }

    public String getPBK (String password, int gpCode)
    {
        byte[] op = new byte[1024];
        String salt = null;
        int iteration = 0;
        byte size =1;
        try {
            op[0]=GET_PBKDF2_GROUP;
            op[1]=size;
            op[2]=(byte) gpCode;
            _csc.send.setPacket(op).write();

            salt = new String(_csc.receive.setAllocate(32).read().getByte());
            iteration = byteArrayToInt(_csc.receive.setAllocate(4).read().getByte());

            System.out.println("Group: " +salt +"  " + iteration);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pbkdf2(password,salt,iteration);

    }

    public String pbkdf2(String password, String salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, 20*8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return new String(Base64.encode(hash,Base64.NO_WRAP));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("error on pbkdf2", e);
        }
    }

    public String SHA(String str)
    {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }
        md.update(str.getBytes());
        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for(int i=0; i<byteData.length; i++) {
            sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public String getFileSHA(String path) {
        Client_Server_Connector css = Client_Server_Connector.getInstance();

        byte[] op = new byte[1024];
        byte size =1;
        String sha = null;
        try {
            op[0]=GET_FILE_SHA_HEADER;
            op[1]=size;
            op[2] = (byte)String.valueOf(path).getBytes().length;
            Function.frontInsertByte(3, String.valueOf(path).getBytes(), op);

            css.send.setPacket(op).write();


            sha = new String(css.receive.setAllocate(64).read().getByte());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sha;
    }


}
