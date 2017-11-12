package Server;

import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.plaf.synth.SynthSpinnerUI;

import Crypto.Base64Coder;

import java.io.*;
import java.net.URLEncoder;

import Function.PacketRule;
import Function.SecurePacketProcessor;
import Function.PacketProcessor;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 29
 * 
 * Name : FileShare
 * Description : It can share file
 * 
 * */

public class Server_FileShare_Receive extends Server_Funtion implements PacketRule
{
	public Server_FileShare_Receive(Server_Client_Activity activity) {
		super(activity);
	}
	
	// Address
	private String _address = "Server_Folder\\Share";
	
	// Instance
	private int _count = 1;
	private int _fileCount = 0;
	private String _fileName = null;
	private long _fileSize = 0;
	private String _userId = null;
	private SecretKey key = null;
	private String hash = null;
	
	// OTP Instance
	private int _oneTime = 1;
	private String _OTP = "";
	private Vector<String> _OTP_List = null;
	private boolean _checkOTP = false;
	private FileOutputStream fos = null;
	private ObjectOutputStream oos = null;
	
	// FileChannel and RandomAccessFile
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	SecurePacketProcessor p = null;
	
	// Methods
	private int sendPacketSize(long fileSize)
	{
		int remainder = (int)(fileSize / FILE_BUFFER_SIZE);
		if((fileSize % FILE_BUFFER_SIZE) > 0)
		{
			remainder++;
		}
		
		return remainder;
	}
	
	private void setFileInformation(byte[] packet)
	{
		_fileCount = packet[1];
		int end = 0;
		
		byte[] sizeTemp = new byte[packet[2]];
		for(int i = 0; i < sizeTemp.length; i++)
		{
			sizeTemp[i] = packet[i + 4];
			end = i+4;
		}
		_fileSize = Long.parseLong(new String(sizeTemp).trim());
		
		byte[] nameTemp = new byte[packet[3]];
		for(int i = 0; i < nameTemp.length; i++)
		{
			nameTemp[i] = packet[i + end + 1];
		}
		byte[] userIdtemp = new byte[packet[500]];
		for(int i=0;i<userIdtemp.length;i++)
		{
			userIdtemp[i] = packet[i+550];
		}

		Server_DataBase db = Server_DataBase.getInstance();
		ResultSet rs = null;
		_userId = new String (userIdtemp).trim();
		_fileName = new String(nameTemp).trim();
		_OTP = _fileName.substring(0, 6);
		_fileName = _fileName.substring(6);
		System.out.println(_userId + _OTP);
		
		hash = md5(_userId.concat(_OTP).getBytes());
		_fileName = hash.concat(_fileName);
		
		rs = db.Query("SELECT aeskey from test where id='" + _userId +"';");
		try {
			rs.next();
			System.out.println(Base64.getDecoder().decode(rs.getString(1).getBytes()));
			System.out.println(Base64.getDecoder().decode(rs.getString(1).getBytes()).length);
			key = new SecretKeySpec(Base64.getDecoder().decode(rs.getString(1).getBytes()),"AES");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*String publickey = null;
		PublicKey pk = null;
		int us = 0;

		rs = db.Query("SELECT uscode, publickey from test WHERE id ='" +_userId+ "';");
		try {
			while(rs.next())
			{
				System.out.println("있어!!!!");
				us = rs.getInt(1);
				publickey = rs.getString(2).trim();
				pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publickey)));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(us);
		System.out.println(pk.getFormat());
		System.out.println(pk.getAlgorithm()+ "\n\n\n");
		System.out.println(pk.getEncoded());
		System.out.println("\n\n\n\n");
		System.out.println(publickey);
		String hash = md5(encrypt(pk, _OTP.getBytes()));*/
		/*_fileName = _fileName.substring(6);
		_fileName =hash.concat(_fileName);
		
		db.Update("INSERT INTO files VALUES('"+ us +"','" +hash + "','" + Base64.getEncoder().encodeToString(encrypt(pk, _OTP.getBytes())).trim()+ "');");
		System.out.println(_fileName);*/
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		File check = new File(_address);
		if(!check.exists())
		{
			check.mkdir();
		}
		setFileInformation(packet);
		_packetMaxCount = 1 + sendPacketSize(_fileSize);
		
		System.out.println(_fileName);
		try 
		{
			_raf = new RandomAccessFile(_address + "\\" + _fileName, "rw");
			_fileChannel = _raf.getChannel();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		_activity.receive.setAllocate(_fileSize);
		
		p = new SecurePacketProcessor(_fileChannel, false);
		p.init(key);
		
		_cutSize = 1;
	}

	@Override
	public void running(int count) throws IOException
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			p.setPacket(_activity.receive.getByte()).write();

			if(count == _packetMaxCount)
			{
				p.close();
			}
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
