package Function;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketProcesser 
{
	private Queue<ByteBuffer> _queue;
	private Queue<Integer> _allocator;
	
	private GatheringByteChannel _output;
	private ScatteringByteChannel _input;
	
	private final int LIMIT_SIZE = 1024;
	
	public PacketProcesser(Object channel, boolean blocking)
	{
		_output = (GatheringByteChannel) channel;
		_input = (ScatteringByteChannel) channel;
		
		if(blocking) { _queue = new LinkedBlockingQueue<ByteBuffer>(); }
		else		 { _queue = new LinkedList<ByteBuffer>();          }
		
		_allocator = new LinkedList<Integer>();
	}
	
	public synchronized int capacity()
	{
		return _queue.size();
	}
	
	public PacketProcesser setAllocate(int size)
	{
		if(size > 1024)
		{
			int remain = size % LIMIT_SIZE;
			for(int i = size / LIMIT_SIZE; i > 0; i--)
			{
				_allocator.add(LIMIT_SIZE);
			}
			if(remain > 0) { _allocator.add(remain); }
		}
		else   
		{
			_allocator.add(size);
		}
		
		return this;
	}
	
	private ByteBuffer allocate(int size)
	{
		if(_allocator.isEmpty())
		{
			return ByteBuffer.allocateDirect(size);
		}
		else
		{
			return ByteBuffer.allocateDirect(_allocator.remove());
		}
	}
	
	public synchronized PacketProcesser setPacket(byte[] packet)
	{
		ByteBuffer buf = allocate(packet.length);
		buf.put(packet);
		buf.flip();
		_queue.add(buf);
		
		return this;
	}
	
	public synchronized PacketProcesser setPacket(ByteBuffer packet)
	{
		packet.flip();
		_queue.add(packet);
		
		return this;
	}
	
	public synchronized ByteBuffer getByteBuf()
	{
		return _queue.remove();
	}
	
	public synchronized byte[] getByte()
	{
		ByteBuffer buf = _queue.remove();
		byte[] array = new byte[buf.remaining()];
		buf.get(array);
		return array;	
	}
	
	
	public synchronized PacketProcesser read()
	{
		try 
		{
			ByteBuffer buf = allocate(LIMIT_SIZE);			
			_input.read(buf);
			buf.flip();
			_queue.add(buf);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
	public synchronized void write()
	{
		try 
		{
			ByteBuffer buf = _queue.remove();
			_output.write(buf);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
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
}
