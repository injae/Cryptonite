package Function;

public interface PacketRule 
{
	int FILE_BUFFER_SIZE = 1024;
	int EVENT_LEN = 5;
	// Event Code
	byte AUTOBACKUP = 1;
	byte LOGIN = 2;
	byte FILE_SHARE_RECEIVE =3;
	byte FILE_SHARE_SEND = 4;
	byte SIGN_UP = 5;
	byte MAKE_OTP = 6;
	//---------------------------------------
	
	// AUTOBACKUP(1) AUTOBACKUP EVENT
	byte FILE = 1;
	byte DIRECTORY = 2;
	//---------------------------------------
	
	// SIGN UP
	byte DUPLICATION_CHECK_FUNCTION =1;
	byte SIGN_UP_FUNCTION = 2;
	
	/*
	 * Order
	 * 1. Event Code, Target + File or directory
	 * 2. File or Directory
	 * 3. Name
	 * 4. FileSize
	 * 
	 * */
}
