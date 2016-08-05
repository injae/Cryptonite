package Server;

import java.nio.*;
import java.nio.channels.*;
import java.util.Vector;
import java.io.*;
import Function.PacketRule;

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
	// Address
	private String _address = "C:\\Server\\Backup";
	
	// Instance
	private int _count = 1;
	private int _fileCount = 0;
	private String _fileName = null;
	private long _fileSize = 0;
	
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
	
	// Constructors
	public Server_FileShare_Receive() { }
	
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
		
		int max = end;
		while(packet[max] != 0)
		{
			max++;
		}
		
		byte[] nameTemp = new byte[packet[3]];
		for(int i = 0; i < nameTemp.length; i++)
		{
			nameTemp[i] = packet[i + end + 1];
		}
		_fileName = new String(nameTemp).trim();
		System.out.println("파일 이름 : " + _fileName);
		System.out.println("파일 용량 : " + _fileSize + " (Byte)");
	}
	
	private void makeOTP()
	{
		int temp;
		
		try 
		{
			if(_oneTime == 1)
			{
				_OTP_List = new Vector<String>();	
				_oneTime++;
			}
			fos = new FileOutputStream("C:\\Server\\OTP\\OTP_List.ser");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(_OTP_List);
			
			FileInputStream fis = new FileInputStream("C:\\Server\\OTP\\OTP_List.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			_OTP_List = (Vector<String>) ois.readObject();
			
			do
			{
				for(int i = 0; i < 6; i++)
				{
					temp = (int)(Math.random()*10);
					_OTP += Integer.toString(temp);
				}
				_checkOTP = true;
				
				for(int j = 0; j < _OTP_List.size(); j++)
				{
					if(_OTP.equals(_OTP_List.get(j)))
					{
						_checkOTP = false;
						break;
					}
				}
				
			} while(_checkOTP == false);
			
			_OTP_List.add(_OTP);
			/*fos = new FileOutputStream("C:\\Server\\OTP\\OTP_List.ser");
			oos = new ObjectOutputStream(fos);*/
			oos.writeObject(_OTP_List);
			
			oos.close();
			fos.close();
			ois.close();
			fis.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("OTP폴더에 OTP_List.ser 파일이 존재하지 않습니다.");
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
		setFileInformation(packet);
		_packetMaxCount = 1 + 1 + sendPacketSize(_fileSize);
		makeOTP();
		
		try 
		{
			_raf = new RandomAccessFile(_address + "\\" + _fileName, "rw");
			_fileChannel = _raf.getChannel();
			_packetCutSize = 1;
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void running(Server_Client_Activity activity)
	{
		System.out.println("NOW FILE_SHARE_RECEIVE RUNNING");		
		if(_count == 1)
		{
			activity._receiveQueue.remove();
			
			byte[] test = new byte[1024];
			test = _OTP.getBytes();
			activity.Sender(test);
			activity.send();
			_count++;
		}
		else
		{
			try
			{
				ByteBuffer buffer;
				buffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
				while(activity.IsReadable())
				{
					_count++;
					buffer.clear();
					buffer.put(activity._receiveQueue.remove());
					
					_fileSize -= FILE_BUFFER_SIZE;
					buffer.flip();
					while(!_fileChannel.isOpen())
					{
						Thread.sleep(1);
					}
					_fileChannel.write(buffer);
					
					if(_fileSize <= 0)
					{
						break;
					}
				}
				System.out.println("Count : " + _count);
				
				if(_count == _packetMaxCount)
				{
					System.out.println(_fileName + " 파일이 수신 완료되었습니다.");
					_fileChannel.close();
					_count = 1;
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
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
