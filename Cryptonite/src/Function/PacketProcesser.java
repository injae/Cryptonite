package Function;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.plaf.synth.SynthSpinnerUI;

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
	
	public PacketProcesser setAllocate(long size)
	{
		if(size > 1024)
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
	
	public PacketProcesser setPacket(byte[] packet, int size)
	{
		byte[] temp = new byte[size];
		
		for(int i =0; i < packet.length; i++)
		{
			temp[i] = packet[i];
		}
		
		ByteBuffer buf = allocate(temp.length);
		buf.put(temp);
		buf.flip();
		_queue.add(buf);
		
		return this;
	}
	
	public PacketProcesser setPacket(byte[] packet)
	{
		ByteBuffer buf = allocate(packet.length);
		buf.put(packet);
		buf.flip();
		_queue.add(buf);
		
		return this;
	}
	
	public PacketProcesser setPacket(ByteBuffer packet)
	{
		if(!packet.hasRemaining()) { packet.flip(); }	
		_queue.add(packet);
		
		return this;
	}
	
	public ByteBuffer getByteBuf()
	{
		return _queue.remove();
	}
	
	public byte[] getByte()
	{
		ByteBuffer buf = _queue.remove();
		byte[] array = new byte[buf.remaining()];
		buf.get(array);
		return array;	
	}
	
	
	public PacketProcesser read()
	{
		try 
		{
			ByteBuffer buf = allocate(LIMIT_SIZE);			
			_input.read(buf);
			buf.flip();
			System.out.println(buf.toString());
			_queue.add(buf);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
	public void write()
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
	
	public boolean isEmpty()
	{
		return _queue.isEmpty();
	}
	
	public boolean isAllocatorEmpty()
	{
		return _allocator.isEmpty();
	}
}
