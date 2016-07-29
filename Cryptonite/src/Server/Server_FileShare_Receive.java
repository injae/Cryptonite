package Server;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
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
	// Instance
	
	
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
	
	@Override
	public void Checker(byte[] packet) 
	{
		_packetMaxCount = 1 + ((packet[1] + 1) * 2);
	}

	@Override
	public void running(Server_Client_Activity activity)
	{
	
	}
}
