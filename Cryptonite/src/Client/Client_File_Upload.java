package Client;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;

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
	
	// Constructors
	public Client_File_Upload()
	{
		_csc = Client_Server_Connector.getInstance();
		//_cfs = new Client_FileSelector();
	}
	
	// Methods
	public void click(String gpCode)
	{
		try 
		{	
			//fileSelection();
			
			for(int i = 0; i < _fileNameArray.length; i++)
			{
				byte[] event = new byte[1024];
				event[0] = FILE_UPLOAD;
				event[1] = (byte)_fileNameArray[i].getBytes().length;
				event[2] = (byte)String.valueOf(_fileSizeArray[i]).getBytes().length;
				Function.frontInsertByte(3, _fileNameArray[i].getBytes(), event);
				Function.frontInsertByte(3 + _fileNameArray[i].getBytes().length, String.valueOf(_fileSizeArray[i]).getBytes(), event);
				Function.frontInsertByte(800, gpCode.getBytes(), event);
				_csc.send.setPacket(event).write();
				
				_raf = new RandomAccessFile(_filePathArray[i], "rw");
				_fileChannel = _raf.getChannel();
				PacketProcessor p = new PacketProcessor(_fileChannel, false);
				p.setAllocate(_fileSizeArray[i]);
				
				while(!p.isAllocatorEmpty())
				{
					_csc.send.setPacket(p.read().getByte()).write();
				}
				p.close();
				System.out.println(_fileNameArray[i] + " 파일이 전송이 완료되었습니다.");
				//_cfs.dispose();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void setInformation(List list)
	{
		_filePathArray = new String[list.size()];
		for(int i = 0; i < list.size(); i++)
		{
			_filePathArray[i] = list.get(i).toString();
		}
		nameExtraction(_filePathArray);
	}
	
	private void nameExtraction(String[] filePathArray)
	{
		_fileNameArray = new String[filePathArray.length];
		for(int i = 0; i < _fileNameArray.length; i++)
		{
			File temp = new File(filePathArray[i]);
			_fileNameArray[i] = temp.getName();
		}
	}
	
	/*private void fileSelection()
	{
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
		
		_fileSizeArray = new long[_filePathArray.length];
		for(int i = 0; i < _filePathArray.length; i++)
		{
			File temp = new File(_filePathArray[i]);
			_fileSizeArray[i] = temp.length();
		}
	}*/
}
