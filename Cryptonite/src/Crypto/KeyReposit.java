package Crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringBufferInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Client.Client_GetGPS;
import Client.Client_Progressbar;
import Function.Function;
import Function.PacketProcessor;

public class KeyReposit extends Thread
{
	private static KeyReposit _singleton = null;
	
	private int _level = 1;
	
	private Client_GetGPS cgp = null;

	private SecretKey _aesKey_lv1 = null; // Default AES Key
	private SecretKey _aesKey_lv2 = null; // AES Key based on MAC address
	private SecretKey _aesKey_lv3 = null; // AES Key based on physical address
											// using GPS sensors
	private SecretKey _rsaKey = null; // AES Key for comunication
	private ServerSocket _server;
	
	private Client_Progressbar _cpb = null;
	
	private boolean flag = true;
	
	private KeyReposit() 
	{
		try 
		{
			_server = new ServerSocket(9999);
			_cpb = new Client_Progressbar(4);
		} catch (IOException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}	
	};
	
	public void logout()
	{
		_aesKey_lv1 = null;
		_aesKey_lv2 = null;
		_aesKey_lv3 = null;
	}
	
	public void run()
	{
		while(flag)
		{
			try 
			{
				Socket socket = _server.accept();
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String path = input.readLine();
				
				if(_aesKey_lv1 == null) { continue; }
				
				new Decrypter(path,_aesKey_lv1).start();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public static KeyReposit getInstance() {
		if (_singleton == null) {
			_singleton = new KeyReposit();
		}
		return _singleton;
	}
	
	public void set_level(int level){
		this._level = level;
	}

	public void set_aesKey(SecretKey aesKey) {
		this._aesKey_lv1 = aesKey;
		cgp = new Client_GetGPS();
	}

	public void set_rsaKey(SecretKey rsaKey) {
		this._rsaKey = rsaKey;
	}

	public SecretKey get_aesKey() {
		SecretKey aesKey = null;
		switch (_level) {
		case 1:
			aesKey = _aesKey_lv1;
			break;
		case 2:
			makeLv2Key();
			aesKey = _aesKey_lv2;
			break;
		case 3:
			makeLv3Key();
			aesKey = _aesKey_lv3;
			break;
		}
		return aesKey;
	}

	public SecretKey get_rsaKey() {
		return _rsaKey;
	}
	
	public String get_fileExtention() {
		String fileExtension = null;
		
		switch (_level) {
		case 1:
			fileExtension = ".cnec";
			break;
		case 2:
			fileExtension = ".cnmc";
			break;
		case 3:
			fileExtension = ".cnac";
			break;
		}
		return fileExtension;
	}

	private void makeLv2Key() {
		
		NetworkInterface ni = null;
		byte[] originalAesKeyBytes = _aesKey_lv1.getEncoded();
		byte[] xorAesKeyBytes = new byte[originalAesKeyBytes.length];
		
		
		try {
			// create lv2 key
			ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] mac = ni.getHardwareAddress();
			
			for(int i =0; i<originalAesKeyBytes.length ; i++){
				if(i < mac.length){
					xorAesKeyBytes[i] = (byte) (originalAesKeyBytes[i] ^ mac[i]);
				} else {
					xorAesKeyBytes[i] = originalAesKeyBytes[i];
				}
			}			
			_aesKey_lv2 = new SecretKeySpec(xorAesKeyBytes, "AES");
			
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void makeLv3Key(){
		
		byte[] originalAesKeyBytes = _aesKey_lv1.getEncoded();
		byte[] xorAesKeyBytes = new byte[originalAesKeyBytes.length];
		
		cgp.getGPS();
		byte[] xposBytes = Function.doubleToByteArray(cgp.get_xpos());
		byte[] yposBytes = Function.doubleToByteArray(cgp.get_ypos());
		
		for(int i =0; i<originalAesKeyBytes.length ; i++){
			if(i < xposBytes.length){
				xorAesKeyBytes[i] = (byte) (originalAesKeyBytes[i] ^ xposBytes[i] ^ yposBytes[i]);
			} else {
				xorAesKeyBytes[i] = originalAesKeyBytes[i];
			}
		}	
		_aesKey_lv3 = new SecretKeySpec(xorAesKeyBytes, "AES");
	}
}
