package Function;

import java.nio.ByteBuffer;

public interface PacketRule 
{
	int FILE_BUFFER_SIZE = 1024;
	
	// Event Code
	byte AUTOBACKUP = 1;
	//---------------------------------------
	
	// AUTOBACKUP(1) AUTOBACKUP EVENT
	byte FILE = 1;
	byte DIRECTORY = 2;
	//---------------------------------------
	
	/*
	 * Order
	 * 1. Event Code, Target + File or directory
	 * 2. File or Directory
	 * 3. Name
	 * 4. FileSize
	 * 
	 * */
}
