package Client;

import java.nio.channels.*;
import java.sql.Ref;
import java.io.*;
import java.util.*;

import javax.crypto.Cipher;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Function.*;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 24
 * 
 * Name : AutoBackup System
 * Description : It can backup encrypted files
 * 
 * */

public class Client_AutoBackup implements PacketRule
{
	// File or Directory
	private File _checkProperty = null;
	private String _fileName = null;
	private long _fileSize = 0;
	
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	
	private String _absoluteDirectory = null;
	private String _protectedFolderName = null;
	private String _serverFolder = null;
	
	private Crypto _crypto = null;
	private KeyReposit _reposit = null;
	
	// Sending Module
	private Client_Server_Connector _csc = null;
	
	// Constructors
	public Client_AutoBackup()
	{
		_reposit = KeyReposit.getInstance();
		_crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, _reposit.get_aesKey()));
		_csc = Client_Server_Connector.getInstance();
		_protectedFolderName = nameTokenizer();
	}
	
	public Client_AutoBackup(String address) 
	{
		_reposit = KeyReposit.getInstance();
		_crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, _reposit.get_aesKey()));
		_csc = Client_Server_Connector.getInstance();
		_protectedFolderName = nameTokenizer(address);
	}
	
	// Methods
	public void autoBackup(String absoluteDirectory)
	{
		_absoluteDirectory = absoluteDirectory;
		_checkProperty = new File(_absoluteDirectory);
		_fileName = _checkProperty.getName();
		_fileSize = _checkProperty.length();
		
		if(_checkProperty.isDirectory())
		{
			byte[] temp = new byte[1024];
			temp[0] = AUTOBACKUP;
			temp[1] = DIRECTORY;
			
			try 
			{
				_csc.send.setPacket(temp).write();
				_serverFolder = new String(_csc.receive.setAllocate(500).read().getByte()).trim();
				treeTokenizer();
				_csc.send.setPacket(_serverFolder.getBytes(), 1024).write();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else if(_checkProperty.isFile())
		{
			try 
			{
				boolean checkExtension = extensionTokenizer();
				boolean fileCopyEnd = false;
				
				if(checkExtension)
				{
					do
					{
						try
						{
							_raf = new RandomAccessFile(_absoluteDirectory, "rw");
														
							RandomAccessFile sraf = new RandomAccessFile(_absoluteDirectory + _reposit.get_fileExtention(), "rw");
							_fileChannel = _raf.getChannel();
							PacketProcessor pr = new PacketProcessor(_fileChannel, false);
							PacketProcessor pw = new PacketProcessor(sraf.getChannel(), false);
							
							byte[] temp = new byte[1024];
							temp[0] = AUTOBACKUP;
							temp[1] = FILE;
							temp[2] = (byte)String.valueOf(_fileSize).getBytes().length;
							temp[3] = (byte)(absoluteDirectory + _reposit.get_fileExtention()).getBytes().length;
							temp[4] = (byte)_protectedFolderName.getBytes().length;
							Function.frontInsertByte(5, String.valueOf(_fileSize).getBytes(), temp);
							Function.frontInsertByte(5 + String.valueOf(_fileSize).getBytes().length, (_absoluteDirectory+_reposit.get_fileExtention()).getBytes(), temp);
							Function.frontInsertByte(900, _protectedFolderName.getBytes(), temp);
							
							_csc.send.setPacket(temp).write();
							pw.setAllocate(_fileSize);
							pr.setAllocate(_fileSize);
							_csc.send.setAllocate(_fileSize); // 
							while(!pr.isAllocatorEmpty())
							{
								_crypto.init(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, _reposit.get_aesKey()));
								byte[] buf = _crypto.endecription(pr.read().getByte());
								_csc.send.setPacket(buf).write();
								pw.setPacket(buf).write();
							}
							pr.close();
							pw.close();
							
							fileCopyEnd = true;
							
						}
						catch (FileNotFoundException e) 
						{
							fileCopyEnd = false;
						}
					} while(!fileCopyEnd);
					_checkProperty.delete();
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean extensionTokenizer()
	{
		StringTokenizer st = new StringTokenizer(_fileName, ".");
		String temp = null;
		
		while(st.hasMoreTokens())
		{
			temp = st.nextToken();
		}
		
		if(temp.equals("cnec") || temp.equals("cnmc") || temp.equals("cnac"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private String nameTokenizer(String address)
	{
		StringTokenizer st = new StringTokenizer(address, "\\");
		String temp = null;
		
		while(st.hasMoreTokens())
		{
			temp = st.nextToken();
		}
		
		return temp;
	}
	
	private String nameTokenizer()
	{
		File tempFile = new File("Cryptonite_Client/log/protectedlog.ser");
		FileReader fr;
		BufferedReader br;
		StringTokenizer st;
		String temp = null;
		try 
		{
			fr = new FileReader(tempFile);
			br = new BufferedReader(fr);
			st = new StringTokenizer(br.readLine(), "\\");
			
			while(st.hasMoreTokens())
			{
				temp = st.nextToken();
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return temp;
	}
	
	private void treeTokenizer()
	{
		StringTokenizer st = new StringTokenizer(_absoluteDirectory, "\\");
		String[] temp = new String[st.countTokens()];
		int i = 0;
		boolean check = false;
		
		while(st.hasMoreTokens())
		{
			temp[i] = st.nextToken();
			i++;
		}
		
		for(int j = 0; j < temp.length; j++)
		{
			if(check)
			{
				_serverFolder += ("\\" + temp[j]);
			}
			if(temp[j].equals(_protectedFolderName))
			{
				check = true;
			}
		}
	}
	
	public String getProtectedName()
	{
		return _protectedFolderName;
	}
}
