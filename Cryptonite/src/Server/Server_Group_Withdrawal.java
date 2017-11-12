package Server;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import Crypto.Crypto_Factory;
import Crypto.aesKeyGenerator;
import Function.Function;

public class Server_Group_Withdrawal extends Server_Funtion {

	String _gpcode;
	
	public Server_Group_Withdrawal(Server_Client_Activity activity) {
		super(activity);
	}

	@Override
	public void Checker(byte[] packet) {
		_packetMaxCount = 1;
		_gpcode = new String(Function.cuttingByte(1, packet)).trim();
	}

	@Override
	public void running(int count) throws IOException {
		try {
			Checker(_activity.getReceiveEvent());
			Server_DataBase db = Server_DataBase.getInstance();
			
			ResultSet rs = db.Query("select *from grouplist where gpcode = " + Server_Code_Manager.codeCutter(_gpcode) + ";");		
			rs.next();	
			
			StringTokenizer gplist = new StringTokenizer(rs.getString(2), ":");
			String save = "";
			while(gplist.hasMoreTokens())
			{
				String temp = gplist.nextToken();
				if(!temp.equals(_activity.getClientCode()))
				{
					if(save.length() != 0) {save += ":" + temp;}
					else 				   {save = temp; }
				}
			}
			
			if(save.length() == 0)
			{
				db.Update("delete from grouplist where gpcode = "+Server_Code_Manager.codeCutter(_gpcode)+";"); 
				db.Update("Delete from groupkey where gpcode = "+Server_Code_Manager.codeCutter(_gpcode)+";"); 
			}
			else 
			{
				db.Update("update grouplist set gplist = '" + save + "' where gpcode = " + Server_Code_Manager.codeCutter(_gpcode) + ";");
				db.Update("delete from groupkey where uscode = '"+Server_Code_Manager.codeCutter(_activity.getClientCode())+"' and gpcode = "+Server_Code_Manager.codeCutter(_gpcode)+";");
				ResultSet rs4 = db.Query("SELECT keynum from grouplist where gpcode ="+Server_Code_Manager.codeCutter(_gpcode)+";");
				rs4.next();
				int keynum = rs4.getInt(1);
				keynum += 1;
				
				aesKeyGenerator kg = new aesKeyGenerator();
				String aeskey = kg.getAesKeyToString();
				
				db.Update("update grouplist set aeskey ='" + aeskey + "',keynum = '"+keynum+"';");
				
				ResultSet rs5= db.Query("select gplist from grouplist where gpcode = "+Server_Code_Manager.codeCutter(_gpcode)+";");
				rs5.next();
				
				String users = rs5.getString(1);
				
				StringTokenizer st = new StringTokenizer(users,":");
				
				while(st.hasMoreTokens())
				{
					String uscode = st.nextToken();
					ResultSet rs6 = db.Query("SELECT publickey from test where uscode = "+Server_Code_Manager.codeCutter(uscode)+";");
					rs6.next();
					String publickey = rs6.getString(1);
					byte[] pk = Base64.getDecoder().decode(publickey);
					
					PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pk));
					
					byte[] encaeskey = Crypto_Factory.create("RSA1024", Cipher.ENCRYPT_MODE, key).doFinal(Base64.getDecoder().decode(aeskey));
					
					db.Update("INSERT INTO groupkey values('"+Server_Code_Manager.codeCutter(_gpcode)+"',"+keynum+",'"+Server_Code_Manager.codeCutter(uscode)+"','"+Base64.getEncoder().encodeToString(encaeskey)+"');");
					
				}
				
				
			}
			
			ResultSet rs3 = db.Query("select * from test where uscode = '" + Server_Code_Manager.codeCutter(_activity.getClientCode()) + "';");
			rs3.next();
			String mygrouplist = rs3.getString(10);
			gplist = new StringTokenizer(mygrouplist, ":");
			save = "";
			while(gplist.hasMoreTokens())
			{
				String temp = gplist.nextToken();
				if(!temp.equals(_gpcode))
				{
					if(save.length() != 0) { save += ":" + temp; }
					else 				   { save = temp; }
				}	
			}
			if(save.length() == 0) { save = "NULL"; }
			db.Update("update test set mygrouplist = '" + save + "' where uscode = " + Server_Code_Manager.codeCutter(_activity.getClientCode()) + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
