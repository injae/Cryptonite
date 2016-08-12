package Server;

import java.util.*;
import java.io.*;

public class Server_MakeOTP extends Server_Funtion
{
	// OTP Instance
	private String _OTP = "";
	private boolean _checkOTP = false;
	private Vector<String> _OTP_List = null;
	
	private FileOutputStream _fos = null;
	private ObjectOutputStream _oos = null;
	
	private int _oneTime = 1;
	
	// Constructors
	public Server_MakeOTP() { }
	
	private void makeOTP()
	{
		int temp;
		
		try 
		{
			if(_oneTime == 1)
			{
				_OTP_List = new Vector<String>();
				_fos = new FileOutputStream("Server_Folder\\OTP\\OTP_List.ser");
				_oos = new ObjectOutputStream(_fos);
				_oos.writeObject(_OTP_List);
				_oneTime++;
			}
			
			FileInputStream fis = new FileInputStream("Server_Folder\\OTP\\OTP_List.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			_OTP_List = (Vector<String>) ois.readObject();
			ois.close();
			fis.close();
			
			do
			{
				_OTP = "";
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
			_oos.writeObject(_OTP_List);
			
			_oos.close();
			_fos.close();
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
	public void Checker(byte[] packet, Server_Client_Activity activity) 
	{
		_activity = activity;
		_packetMaxCount = 1 + 1;
		_packetCutSize = 1;
	}

	@Override
	public void running() 
	{
		makeOTP();
		_activity.receive.getByte();	// garbage delete
		_activity.send.setPacket(_OTP.getBytes()).write();
		System.out.println("OTP Sending FINISH");
	}
}
