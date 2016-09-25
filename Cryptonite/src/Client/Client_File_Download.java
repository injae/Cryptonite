package Client;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Function.PacketProcessor;
import Function.PacketRule;

public class Client_File_Download implements PacketRule
{
	Crypto _crypto;
	KeyReposit _reposit;
	public void requestFile(String path, String targetpath, SecretKey key)
	{
		try 
		{
			Charset cs = Charset.forName("UTF-8");
			
			targetpath = targetpath.substring(0,targetpath.length() - 5);
			_reposit = KeyReposit.getInstance();
			_crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, key));
			_crypto.init(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, key));
			
			Client_Server_Connector csc = Client_Server_Connector.getInstance();
			byte[] event = new byte[1024];
			event[0] = FILE_DOWNLOAD;
			csc.send.setPacket(event).write();
			
			csc.send.setPacket(cs.encode(path).array(), 500).write();
			
			long fileSize =  Long.parseLong(new String(csc.receive.setAllocate(500).read().getByte()).trim());
			csc.receive.setAllocate(fileSize);
			
			RandomAccessFile raf  = new RandomAccessFile(targetpath, "rw");
			PacketProcessor p = new PacketProcessor(raf.getChannel(), false);
			
			p.setAllocate(fileSize);
			while(!csc.receive.isAllocatorEmpty())
			{	
				p.setPacket(_crypto.endecription(csc.receive.read().getByte())).write();
			}
			p.close();
		}
		catch (NumberFormatException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
