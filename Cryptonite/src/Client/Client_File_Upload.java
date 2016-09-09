package Client;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.crypto.Cipher;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Function.PacketRule;
import Function.Function;
import Function.PacketProcessor;

public class Client_File_Upload implements PacketRule
{
	// Instance
	private String[] _fileNameArray;
	private String[] _filePathArray;
	private long[] _fileSizeArray;
	
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	
	// Another Class
	private Client_Server_Connector _csc = null;
	private Client_FileSelector _cfs = null;
	
	private Crypto _crypto = null;
	private KeyReposit _reposit = null;
	
	// Constructors
	public Client_File_Upload()
	{
		_reposit = KeyReposit.getInstance();
		_crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, _reposit.get_aesKey()));
		_csc = Client_Server_Connector.getInstance();
		_cfs = new Client_FileSelector();
	}
	
	// Methods
	public void click(String gpCode)
	{
		try 
		{	
			fileSelection();
			ByteBuffer bb;
			Charset cs = Charset.forName("UTF-8");
			for(int i = 0; i < _fileNameArray.length; i++)
			{
				bb = cs.encode(_fileNameArray[i]);
				byte[] event = new byte[1024];
				event[0] = FILE_UPLOAD;
				event[1] = (byte)bb.limit();
				event[2] = (byte)String.valueOf(_fileSizeArray[i]).getBytes().length;
				Function.frontInsertByte(3, bb.array(), event);
				Function.frontInsertByte(3 + bb.limit(), String.valueOf(_fileSizeArray[i]).getBytes(), event);
				Function.frontInsertByte(800, gpCode.getBytes(), event);
				_csc.send.setPacket(event).write();
				
				_raf = new RandomAccessFile(_filePathArray[i], "rw");
				_fileChannel = _raf.getChannel();
				PacketProcessor p = new PacketProcessor(_fileChannel, false);
				p.setAllocate(_fileSizeArray[i]);
				
				while(!p.isAllocatorEmpty())
				{
					_csc.send.setPacket(_crypto.endecription(p.read().getByte())).write();
				}
				p.close();
				System.out.println(_fileNameArray[i] + " 파일이 전송이 완료되었습니다.");
				_cfs.dispose();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void fileSelection()
	{
		Charset cs = Charset.forName("UTF-8");
		_cfs.fileFinderON();
		while(!_cfs.getSelectionFinish())
		{
			try 
			{
				Thread.sleep(1);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		_fileNameArray = _cfs.getFileNames();
		_filePathArray = _cfs.getFilePaths();
		
		for(int i = 0; i < _fileNameArray.length; i++)
		{
			_fileNameArray[i] += ".cnec";
		}
		
		_fileSizeArray = new long[_filePathArray.length];
		for(int i = 0; i < _filePathArray.length; i++)
		{
			File temp = new File(_filePathArray[i]);
			_fileSizeArray[i] = temp.length();
		}
	}
}
