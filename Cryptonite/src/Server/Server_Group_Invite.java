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

public class Server_Group_Invite extends Server_Funtion 
{
	private String _id;
	private String _gpCode;
	private boolean _passCheck;
	
	public Server_Group_Invite(Server_Client_Activity activity) 
	{
		super(activity);
	}

	private void setInformation(byte[] packet)
	{
		int end = 0;
		byte[] idTemp = new byte[packet[1]];
		for(int i = 0; i < idTemp.length; i++)
		{
			idTemp[i] = packet[i + 3];
			end = i + 3;
		}
		_id = new String(idTemp).trim();
		
		byte[] gpCodeTemp = new byte[packet[2]];
		for(int i = 0; i < gpCodeTemp.length; i++)
		{
			gpCodeTemp[i] = packet[i + end + 1];
		}
		_gpCode = new String(gpCodeTemp).trim();
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		setInformation(packet);
		_packetMaxCount = 1;
		_cutSize = 1;
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) 
		{ 
			Checker(_activity.getReceiveEvent()); 
			try 
			{
				_passCheck = true;
				Server_DataBase db = Server_DataBase.getInstance();
				ResultSet rs = db.Query("select * from test where id = '" + _id + "';");
				rs.next();
				String uscode = "@" + rs.getString(6);
				
				ResultSet rs2 = db.Query("select * from grouplist where gpcode = " + Server_Code_Manager.codeCutter(_gpCode) + ";");
				rs2.next();
				String gplist = rs2.getString(2);
				StringTokenizer st = new StringTokenizer(gplist, ":");
				while(st.hasMoreTokens())
				{
					if(st.nextToken().equals(uscode))
					{
						_passCheck = false;
					}
				}
				if(_passCheck)
				{
					gplist += ":" + uscode;
					db.Update("update grouplist set gplist = '" + gplist + "' where gpcode = " + Server_Code_Manager.codeCutter(_gpCode) + ";");
					
					ResultSet rs3 = db.Query("select * from test where id = '" + _id + "';");
					rs3.next();
					String mygrouplist = rs3.getString(10);
					if(mygrouplist.equals("NULL"))
					{
						mygrouplist = _gpCode;
					}
					else
					{
						mygrouplist += ":" + _gpCode;
					}
					db.Update("update test set mygrouplist = '" + mygrouplist + "' where uscode = " + Server_Code_Manager.codeCutter(uscode) + ";");
					
					ResultSet rs4 = db.Query("select * from test where id = '" + _id + "';");
					rs4.next();
					String pubkey = rs4.getString(14);
					byte[] pubKey = Base64.getDecoder().decode(pubkey);
					
					PublicKey pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
					
					ResultSet rs5 = db.Query("select * from grouplist where gpcode = " + Server_Code_Manager.codeCutter(_gpCode) + ";");
					rs5.next();
					
					byte[] groupkey = Crypto_Factory.create("RSA1024", Cipher.ENCRYPT_MODE, pk).doFinal(Base64.getDecoder().decode(rs5.getString(4)));
					
					//System.out.println();
					db.Update("INSERT INTO groupkey values('"+ Server_Code_Manager.codeCutter(_gpCode) +"'," + rs5.getInt(11) + ",'" + Server_Code_Manager.codeCutter(uscode) + "','" + Base64.getEncoder().encodeToString(groupkey) + "')");
					
					
					_activity.send.setPacket("TRUE".getBytes(), 100).write();
				}
				else
				{
					_activity.send.setPacket("FALSE".getBytes(), 100).write();
				}
			} 
			catch (SQLException e) 
			{
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
		else
		{
			
		}
	}
}
