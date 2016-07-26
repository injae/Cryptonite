package Function;

import java.nio.ByteBuffer;

public interface PacketRule 
{
	// Event kind
	byte AUTOBACKUP = 1;
	//---------------------------------------
	
	// AUTOBACKUP(1) AUTOBACKUP EVENT
	byte FILE = 1;
	byte DIRECTORY = 2;
	//---------------------------------------
}
