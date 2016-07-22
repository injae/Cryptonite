package Client;

import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.io.*;
import java.util.*;
import Function.NIO_FileManager;

public class Client_AutoBackup extends Thread
{
	// SocketChannel
	private SocketChannel socketChannel = null;
	
	// 
	
	// Constructors
	
	// Methods
	public synchronized void run()
	{
		try 
		{
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(true);
			System.out.println("Complete Server Connecting !!");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
