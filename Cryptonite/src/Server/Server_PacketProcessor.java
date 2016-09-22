package Server;

import java.io.IOException;

import Function.PacketProcessor;
import Function.SecurePacketProcessor;

public class Server_PacketProcessor extends SecurePacketProcessor {

	public Server_PacketProcessor(Object channel, boolean blocking) 
	{
		super(channel, blocking);
	}

	
	@Override
	public void write() throws IOException 
	{
		byte[] array =_queue.remove();
		allocate(array.length);
		_buffer.put(array);
		Server_Administrator.getInstance().inoutUpdate(("write : " + _buffer.toString()));

		_buffer.flip();
		while(_buffer.hasRemaining())
		{
			Server_Administrator.getInstance().packetSizeUpdate(_output.write(_buffer));
		}
	}
	@Override
	public PacketProcessor read() throws IOException 
	{
		allocate(LIMIT_SIZE);
		Server_Administrator.getInstance().packetSizeUpdate(_input.read(_buffer));
		while(_buffer.hasRemaining())
		{
			Server_Administrator.getInstance().packetSizeUpdate(_input.read(_buffer));
			if(_buffer.remaining() == _buffer.limit())
			{
				System.out.println("This packet is wrong packet"); break;
			}
		}
		Server_Administrator.getInstance().inoutUpdate("read  : " + _buffer);	//socket test line
		_buffer.flip();
		
		byte[] array = new byte[_buffer.remaining()];
		_buffer.get(array);
		
		_queue.add(array);

		return this;
	}
}
