package Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import Function.PacketProcessor;
import Function.PacketRule;

public class Server_File_Upload extends Server_Funtion implements PacketRule
{
	// Instance
	private String _address;
	private String _fileName;
	private long _fileSize;
	private String _gpCode;
	
	private PacketProcessor _p = null;
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	
	public Server_File_Upload(Server_Client_Activity activity) 
	{
		super(activity);
	}
	
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
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(packet[1]);
		int end = 0;
		byte[] nameTemp = new byte[packet[1]];
		for(int i = 0; i < nameTemp.length; i++)
		{
			nameTemp[i] = packet[i + 3];
			end = i + 3;
		}
		bb.put(nameTemp); bb.flip();
		_fileName = cs.decode(bb).toString();
		
		byte[] sizeTemp = new byte[packet[2]];
		for(int i = 0; i < sizeTemp.length; i++)
		{
			sizeTemp[i] = packet[i + end + 1];
		}
		_fileSize = Long.parseLong(new String(sizeTemp).trim());
		
		byte[] gpCodeTemp = new byte[100];
		for(int i = 0; i < gpCodeTemp.length; i++)
		{
			gpCodeTemp[i] = packet[i + 800];
		}
		_gpCode = new String(gpCodeTemp).trim();
	}

	@Override
	public void Checker(byte[] packet) 
	{
		setFileInformation(packet);
		
		if (_fileName.substring(_fileName.length()-5,_fileName.length()).equals(".cnmc"))
			_packetMaxCount = 1 + 1 + sendPacketSize(_fileSize-64);
		else
			_packetMaxCount = 1 + sendPacketSize(_fileSize);
		
		try 
		{
			_address = "Server_Folder\\Backup\\" + _gpCode;
			
			Server_DataBase db= Server_DataBase.getInstance();
			ResultSet rs=db.Query("SELECT keynum from grouplist where gpcode='"+Server_Code_Manager.codeCutter(_gpCode) +"';");
			
			rs.next();
			String keynum= rs.getString(1);
			
			if(_fileName.endsWith("cnmc")){
				_raf = new RandomAccessFile(_address + "\\" +_fileName, "rw");
			}
			else
			{
				File f = new File(_address);
				File[] files = f.listFiles();
				
				for (int i =0; i<files.length;i++)
				{  System.out.println("hihihihi");
					String fname = files[i].getName();
					
					StringTokenizer st2 = new StringTokenizer(fname, "#");
					String filename = "";
					st2.nextToken();
					while(st2.hasMoreTokens())
					{
						filename +=st2.nextToken();
					}
					System.out.println("fname : "+fname+",,,,,,"+filename+"fileName : "+_fileName);
					if(filename.equals(_fileName)){
						files[i].delete();
					}
				}
				
				_raf = new RandomAccessFile(_address + "\\" + keynum+"#"+_fileName, "rw");
			}	
			
			_fileChannel = _raf.getChannel();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (_fileName.substring(_fileName.length()-5, _fileName.length()).equals(".cnmc"))
		{
			_activity.receive.setAllocate(64).setAllocate(_fileSize-64);
		}
		else
			_activity.receive.setAllocate(_fileSize);
		
		_p = new PacketProcessor(_fileChannel, false);
		_cutSize = 1;
	}

	@Override
	public void running(int count) throws IOException
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			_p.setPacket(_activity.receive.getByte()).write();
			
			if(count == _packetMaxCount)
			{
				_p.close();
			}
		}
	}
}
