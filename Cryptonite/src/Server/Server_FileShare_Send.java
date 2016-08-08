package Server;

import java.util.*;
import java.io.*;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 08 - 08
 * 
 * Name : FileShare
 * Description : It can share file
 * 
 * */

public class Server_FileShare_Send extends Server_Funtion
{
	// OTP Instance
	private String _OTP = null;
	private FileInputStream _fis = null;
	private ObjectInputStream _ois = null;
	private Vector<String> _OTP_List = null;
	private boolean _temporaryFlag = false;
	private String _downloadFlag = null;
	
	// Methods
	private void OTP_Check()
	{
		for(int i = 0; i < _OTP_List.size(); i++)
		{
			if(_OTP.equals(_OTP_List.get(i)))
			{
				_temporaryFlag = true;
			}
		}
		if(_temporaryFlag == true)
		{
			_downloadFlag = "TRUE";
		}
		else if(_temporaryFlag == false)
		{
			_downloadFlag = "FALSE";
		}
	}
	
	private void fileNameTokenizer()
	{
		
	}
	
	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = 1 + 1;
		_packetCutSize = 1;
		
		try 
		{
			_fis = new FileInputStream("C:\\Server\\OTP\\OTP_List.ser");
			_ois = new ObjectInputStream(_fis);
			_OTP_List = (Vector<String>) _ois.readObject();
			_ois.close();
			_fis.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
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
	public void running(Server_Client_Activity activity) 
	{
		byte[] temp = new byte[1024];
		temp = activity._receiveQueue.remove();
		_OTP = new String(temp).trim();
		OTP_Check();
		activity.Sender(_downloadFlag.getBytes());
		activity.send();	// temporary
		
		if(_temporaryFlag == true)
		{
			
		}
	}
}
