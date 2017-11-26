package Client;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import Function.PacketRule;

public class Client_PBKDF2 implements PacketRule {
	
	private static Client_PBKDF2 pbk = null;
	
	private Client_PBKDF2()
	{
		
	}
	
	public static Client_PBKDF2 getInstance()
	{
		if (pbk == null)
			pbk = new Client_PBKDF2();
		return pbk;
	}
	
	private String getPassword(String name) {
		String password = "";
		JPasswordField passwordField = new JPasswordField();
		Object[] obj = {"Input "+ name + " Password\nWarning!!\nIf you forget your password, you will not be able to decrypt the file.",passwordField};
		if (JOptionPane.showOptionDialog(null, obj, "Password", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.YES_OPTION) {
			password = new String(passwordField.getPassword());
		}
		return password;
	}
	
	private String getPBK (String password)
	{
		
		Client_Server_Connector _csc = Client_Server_Connector.getInstance();
		
		byte[] op = new byte[1024];
		String salt = null;
		int iteration = 0;
		byte size =1;
		try {
			op[0]=GET_PBKDF2;
			op[1]=size;
			_csc.send.setPacket(op).write();
			
			salt = new String(_csc.receive.setAllocate(32).read().getByte());
			iteration = byteArrayToInt(_csc.receive.setAllocate(4).read().getByte());
			
			System.out.println(salt +"  " + iteration);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pbkdf2(password,salt,iteration);
		
	}
	
	public  int byteArrayToInt(byte bytes[]) {
		return ((((int)bytes[0] & 0xff) << 24) |
				(((int)bytes[1] & 0xff) << 16) |
				(((int)bytes[2] & 0xff) << 8) |
				(((int)bytes[3] & 0xff)));
	} 
    public String pbkdf2(String password, String salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, 20*8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return new String(Base64.getEncoder().encode(hash));
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
}
