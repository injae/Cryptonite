package Function;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class PacketProcessor 
{
	private Queue<byte[]> _queue;
	private Queue<Integer> _allocator;
	
	private GatheringByteChannel _output;
	private ScatteringByteChannel _input;
	
	private final int LIMIT_SIZE = 1024;
	private ByteBuffer _buffer;
	
	public PacketProcessor(Object channel, boolean blocking)
	{
		_output = (GatheringByteChannel) channel;
		_input = (ScatteringByteChannel) channel;
		
		_buffer = ByteBuffer.allocateDirect(1024);
		
		if(blocking) { _queue = new LinkedBlockingQueue<byte[]>(); }
		else		 { _queue = new LinkedList<byte[]>();          }
		
		_allocator = new LinkedList<Integer>();
	}
	
	public int capacity()
	{
		return _queue.size();
	}
	
	public int allocatorCapacity()
	{
		return _allocator.size();
	}
	
	public PacketProcessor setAllocate(long size)
	{
		if(size > LIMIT_SIZE)
		{
			long remain = size % LIMIT_SIZE;
			for(long i = size / LIMIT_SIZE; i > 0; i--)
			{
				_allocator.add(LIMIT_SIZE);
			}
			if(remain > 0) { _allocator.add((int)remain);}
		}
		else   
		{
			_allocator.add((int)size);
		}
		
		return this;
	}
	
	private void allocate(int size)
	{
		_buffer.clear();
		if(_allocator.isEmpty())
		{
			_buffer.limit(size);
		}
		else
		{
			_buffer.limit(_allocator.remove());
		}
	}
	
	public PacketProcessor setPacket(byte[] packet, int size)
	{
		byte[] temp = new byte[size];
		
		for(int i =0; i < packet.length; i++)
		{
			temp[i] = packet[i];
		}
		_queue.add(temp);
		
		return this;
	}
	
	public PacketProcessor setPacket(byte[] packet)
	{
		_queue.add(packet);
		
		return this;
	}
	
	public PacketProcessor setPacket(ByteBuffer packet)
	{
		if(!packet.hasRemaining()) { packet.flip(); }	
		byte[] array = new byte[packet.remaining()];
		packet.get(array);
		_queue.add(array);
		
		return this;
	}
	
	public ByteBuffer getByteBuf()
	{
		byte[] array =_queue.remove();
		ByteBuffer buf = ByteBuffer.allocate(array.length);
		buf.put(array);
		buf.flip();
		return buf;
	}
	
	public byte[] getByte()
	{	
		return _queue.remove();
	}
	
	public PacketProcessor read() throws IOException
	{
		allocate(LIMIT_SIZE);
		int size = _input.read(_buffer);
		while(_buffer.hasRemaining())
		{
			_input.read(_buffer);
		}
		_buffer.flip();
		
		byte[] array = new byte[_buffer.remaining()];
		_buffer.get(array);
		
		_queue.add(array);

		return this;
	}

	public void write() throws IOException
	{
		byte[] array =_queue.remove();
		allocate(array.length);
		_buffer.put(array);
		_buffer.flip();
		while(_buffer.hasRemaining())
		{
			_output.write(_buffer);
		}
	}
	
	public void close()
	{
		try 
		{
			_output.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean isEmpty()
	{
		return _queue.isEmpty();
	}
	
	public boolean isAllocatorEmpty()
	{
		return _allocator.isEmpty();
	}
}
