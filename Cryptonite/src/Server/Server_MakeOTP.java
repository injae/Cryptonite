package Server;

import java.util.*;
import java.io.*;

public class Server_MakeOTP extends Server_Funtion
{
	// OTP Instance
	private String _OTP = "";
	private boolean _checkOTP = false;
	private Vector<String> _OTP_List = null;
	
	private FileInputStream _fis = null;
	private ObjectInputStream _ois = null;
	
	private FileOutputStream _fos = null;
	private ObjectOutputStream _oos = null;
	
	// Constructors
	public Server_MakeOTP()
	{
		_OTP_List = new Vector<String>();
		makeOTP();
	}
	
	private void makeOTP()
	{
		int temp;
		
		try 
		{
			_fos = new FileOutputStream("C:\\Server\\OTP\\OTP_List.ser");
			_oos = new ObjectOutputStream(_fos);
			_oos.writeObject(_OTP_List);
			
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
			_oos.writeObject(_OTP_List);
			
			_oos.close();
			_fos.close();
			_ois.close();
			_fis.close();
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
	
	public String getOTP()
	{
		return _OTP;
	}

	@Override
	public void Checker(byte[] packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void running(Server_Client_Activity activity) {
		// TODO Auto-generated method stub
		
	}
}
