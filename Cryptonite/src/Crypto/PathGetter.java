package Crypto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class PathGetter {

	private ServerSocket server;
 	private Socket socket;
	
	private InputStream is;
	private OutputStream os;
	

	public PathGetter() 
	{
		try 
		{
			server = new ServerSocket(9999);	
			
		} catch (IOException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}
	
	public void accept()
	{
		try 
		{
			socket = server.accept();

			is = socket.getInputStream();
			os = socket.getOutputStream();
			
		} catch (IOException e) {
			// TODO 자동 생성된 catch 블록
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
        socket.close();
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
	
	
	public void close() throws IOException
	{
		socket.close();
		server.close();
	}
}
