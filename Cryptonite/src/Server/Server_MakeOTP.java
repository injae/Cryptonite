package Server;

import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

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

	private void makeOTP()
	{
		int temp;
		
		try 
		{
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
					if(new String(encrypt(_activity.getPublicKey(), _OTP.getBytes())).concat("@" + Integer.toString(Server_Code_Manager.codeCutter(_activity.getClientCode()))).equals(_OTP_List.get(j)))
					{
						_checkOTP = false;
						break;
					}
				}
				
			} while(!_checkOTP);
			
			_OTP_List.add(_OTP);
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
		}
	}

	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = 1;
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) 
		{ 
			Checker(_activity.getReceiveEvent());
			makeOTP();
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
}
