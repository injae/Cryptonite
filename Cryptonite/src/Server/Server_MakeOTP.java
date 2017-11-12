package Server;

import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Crypto.Base64Coder;

import java.io.*;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server_MakeOTP extends Server_Funtion
{
	public Server_MakeOTP(Server_Client_Activity activity) {
		super(activity);
		// TODO �ڵ� ������ ������ ����
	}

	// OTP Instance
	private String _OTP = "";
	private boolean _checkOTP = false;
	private ArrayList<String> _OTP_List = null;
	
	private FileOutputStream _fos = null;
	private ObjectOutputStream _oos = null;
	
	private int _oneTime = 1;
	private String _userId = null;
	private int us = 0;

	private void makeOTP()
	{
		int temp;
		
		try 
		{
			Server_DataBase db = Server_DataBase.getInstance();
			ResultSet rs = null;

			
			rs = db.Query("SELECT id, uscode from test where id='"+_userId+"';");
			if (rs == null)
				System.out.println("Daodjawdpaw");
			
			if (rs.next())
				us = rs.getInt(2);
			else
				us = 0;
			
			
			if(_oneTime == 1)
			{
				_OTP_List = new ArrayList<String>();
				_fos = new FileOutputStream("Server_Folder\\OTP\\OTP_List.ser");
				_oos = new ObjectOutputStream(_fos);
				_oos.writeObject(_OTP_List);
				_oneTime++;
			}
			
			FileInputStream fis = new FileInputStream("Server_Folder\\OTP\\OTP_List.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			_OTP_List = (ArrayList<String>) ois.readObject();
			ois.close();
			fis.close();
			
			do
			{
				_OTP = "";
				for(int i = 0; i < 6; i++)
				{
					temp = (int)(Math.random()*10);
					_OTP += Integer.toString(temp);
				}
				_checkOTP = true;
				
				
				for(int j = 0; j < _OTP_List.size(); j++)
				{
					if(md5((_userId + _OTP).getBytes()).equals(_OTP_List.get(j)))
					{
						_checkOTP = false;
						break;
					}
				}
				
			} while(!_checkOTP);
			
			_OTP_List.add(md5((_userId + _OTP).getBytes()));
			/*fos = new FileOutputStream("C:\\Server\\OTP\\OTP_List.ser");
			oos = new ObjectOutputStream(fos);*/
			_oos.writeObject(_OTP_List);
			
			_oos.close();
			_fos.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void Checker(byte[] packet) 
	{
		byte[] userIdtemp = new byte[packet[500]];
		for(int i=0;i<userIdtemp.length;i++)
		{
			userIdtemp[i] = packet[i+550];
		}
		_userId = new String (userIdtemp).trim();
		_packetMaxCount = 1;
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) 
		{ 
			Checker(_activity.getReceiveEvent());
			makeOTP();
			if (us == 0)
				_activity.send.setPacket(new String("0").getBytes(),1024).write();
			else
				_activity.send.setPacket(_OTP.getBytes(),1024).write();
		}
		else
		{
			
		}
	}
	public byte[] encrypt(PublicKey key, byte[] plaintext)
	{
	    Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
 
			cipher.init(Cipher.ENCRYPT_MODE, key);  

			return cipher.doFinal(plaintext);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	    
	}
	
	@SuppressWarnings("deprecation")
	public String md5(byte[] _password) {
		MessageDigest _md = null;
		try {
			_md = MessageDigest.getInstance("MD5");
			byte[] temp = _password;
			_md.update(temp);
			return URLEncoder.encode(new String(Base64Coder.encode(_md.digest())));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}
}
