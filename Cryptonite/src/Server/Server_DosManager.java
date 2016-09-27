package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_DosManager
{
	private ServerSocket server;
 	private Socket socket;
	
	private InputStream is;
	private OutputStream os;
	
	private String _id;
	
	public Server_DosManager(String id) 
	{	
		try 
		{
			server = new ServerSocket(7007);
			programRun();
			socket = server.accept();

			System.out.println("立加");
			is = socket.getInputStream();
			os = socket.getOutputStream();
			
			_id = id;
			if(receive().equals("id"));
			{
				send("1");
				send(_id);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receive() throws IOException
	{
		byte[] lenBytes = new byte[4];
        is.read(lenBytes, 0, 4);
        int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                  ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
        
        byte[] receivedBytes = new byte[len];
        is.read(receivedBytes, 0, len);
        return new String(receivedBytes, 0, len);
	}
	
	public void send(String msg) throws IOException
	{
        byte[] toSendBytes = msg.getBytes();
        int toSendLen = toSendBytes.length;
        
        byte[] toSendLenBytes = new byte[4];
        toSendLenBytes[0] = (byte)(toSendLen & 0xff);
        toSendLenBytes[1] = (byte)((toSendLen >> 8) & 0xff);
        toSendLenBytes[2] = (byte)((toSendLen >> 16) & 0xff);
        toSendLenBytes[3] = (byte)((toSendLen >> 24) & 0xff);
        
        os.write(toSendLenBytes);
        os.write(toSendBytes);
	}


	public boolean isClosed()
	{
		return socket.isClosed();
	}
	
	private void programRun()
	{
		try 
		{
			String path  = "\"" + "C:\\Users\\user\\git\\Cryptonite\\Cryptonite\\Cryptonite_Server_Manager\\ConsoleApplication15\\bin\\Debug\\ConsoleApplication15.exe" + "\"";
			Runtime.getRuntime().exec("explorer.exe " + path);
		} catch (IOException e) {
			// TODO 磊悼 积己等 catch 喉废
			e.printStackTrace();
		}
	}
	
	public void reConnect()
	{
		try 
		{
			programRun();
			Socket bsocket = server.accept();
			
			socket.close();
			socket = bsocket;
			System.out.println("立加");
			
			is = socket.getInputStream();
			os = socket.getOutputStream();
			
			if(receive().equals("id"));
			{
				send("1");
				send(_id);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() throws IOException
	{
		socket.close();
		server.close();
	}
	
	
}
