package Server;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class Server_File_ListSender extends Server_Funtion 
{
	private byte _mod;
	private String _name;
	private String _folderName;
	private Queue<String> _fileList = new LinkedList<String>();

	public Server_File_ListSender(Server_Client_Activity activity) {
		super(activity);
	}

	
	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = 1;
		_cutSize = 1;
	 	_mod = packet[1];
	 	if(_mod == 1)
	 	{
	 		byte[] temp = new byte[packet.length - 2];
		 	for(int i = 0; i < packet.length - 2; i++)
		 	{
		 		temp[i] = packet[i + 2];
		 	}
		 	_name = new String(temp).trim();
	 	}
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) 
		{
			Checker(_activity.getReceiveEvent());
			new ListSender().start();
		}
		else
		{
			
		}
	}

	private void searchFolder()
	{
		try 
		{
			switch(_mod)
			{
			case 1:
				ResultSet rs = Server_DataBase.getInstance().Query("select *from grouplist where gpname = '" + _name + "';");
				rs.next();
				_folderName = "Server_Folder//Backup//$" + rs.getInt(1);
				break;
			case 2:
				_folderName = "Server_Folder//Backup//"+_activity.getClientCode();
				break;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	class ListSender extends Thread
	{
		private int _fileCount = 0;
		
		public void run()
		{
			Charset cs = Charset.forName("UTF-8");
			searchFolder();
			readFolder(_folderName);
			_fileCount = _fileList.size();
			System.out.println("");
			
			try 
			{
				_activity.send.setPacket(String.valueOf(_fileCount).getBytes(), 100).write();
				while(!_fileList.isEmpty())
				{
					_activity.send.setPacket(cs.encode(_fileList.remove()).array(), 1024).write();
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	
		private void readFolder(String path)
		{
			File _myFolder = new File(path);
			
			File[] fileList = _myFolder.listFiles();
			
			for(int i =0; i < fileList.length; i++)
			{
				if(fileList[i].isFile())
				{
					_fileList.add(fileList[i].getPath());
				}
				else if(fileList[i].isDirectory())
				{
					_fileList.add(fileList[i].getPath());
					readFolder(fileList[i].getPath());
				}
			}
		}
	}
	
	
}
