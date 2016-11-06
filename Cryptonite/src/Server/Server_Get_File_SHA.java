package Server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server_Get_File_SHA extends Server_Funtion {

	public Server_Get_File_SHA(Server_Client_Activity activity) {
		super(activity);
	}

	Server_DataBase db = Server_DataBase.getInstance();
	byte len =0;
	String path =null;
	@Override
	public void Checker(byte[] packet) {
		_packetMaxCount = 1;
		len = packet[2];
		path = new String(packet,3,len);
	}

	@Override
	public void running(int count) throws IOException {

		Checker(_activity.getReceiveEvent());
		
		char[] sha = new char[64];
		byte[] temp = new byte[64];
		
		System.out.println(path);
		File f = new File(path);
		FileReader fr = new FileReader(f);
		fr.read(sha, 0, 64);
		fr.close();
		
		System.out.println(sha);
		System.out.println(sha.length);
		
		temp = String.valueOf(sha).getBytes();
		
		System.out.println(temp);
		System.out.println(temp.length);
		
		_activity.send.setPacket(temp,64).write();

			
		}
	
	public  byte[] intToByteArray(int value) {
		byte[] byteArray = new byte[4];
		byteArray[0] = (byte)(value >> 24);
		byteArray[1] = (byte)(value >> 16);
		byteArray[2] = (byte)(value >> 8);
		byteArray[3] = (byte)(value);
		return byteArray;
	}

}
