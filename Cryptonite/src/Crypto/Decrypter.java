package Crypto;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.xml.bind.SchemaOutputResolver;

import Client.Client_Progressbar;
import Function.PacketProcessor;

public class Decrypter extends Thread
{
	private String path;
	private SecretKey key;
	
	public Decrypter(String path, SecretKey key) 
	{
		this.path = path;
		this.key = key;
	}

	public void run()
	{
		try 
		{
			Client_Progressbar cpb = new Client_Progressbar(4);
			Crypto crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, key));
			
			RandomAccessFile Rraf  = new RandomAccessFile(path, "rw");
			PacketProcessor rp = new PacketProcessor(Rraf.getChannel(), false);
			
			RandomAccessFile Wraf  = new RandomAccessFile(path.substring(0, path.length()-5), "rw");
			PacketProcessor wp = new PacketProcessor(Wraf.getChannel(), false);
			
			rp.setAllocate(new File(path).length());
			wp.setAllocate(new File(path).length());
			
			cpb.UI_ON();
			while(!rp.isAllocatorEmpty())
			{
				crypto.init(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, key));
				wp.setPacket(crypto.endecription(rp.read().getByte())).write();	
			}
			wp.close();
			rp.close();
			new File(path).delete();
			cpb.UI_OFF();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
