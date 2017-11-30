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
    byte LOGOUT	= 7;
    byte MAKE_GROUP = 8;
    byte EVENT = 9;
    byte FILE_LIST_REQUEST = 10;
    byte FILE_DOWNLOAD = 11;
    byte FILE_UPLOAD = 12;
    byte DELETE_GROUP = 13;
    byte GROUP_INVITE = 14;
    byte KEY_EXCHANGE = 15;
    byte GROUP_WITHDRAWAL = 16;
    byte GROUP_SEARCH = 17;
    byte SHOW_GROUP = 18;
    byte FIND_CAPTAIN = 19;
    byte GET_GPS = 20;
    byte GET_GROUP_KEY = 21;
    byte FOLDER_LIST = 22;
    byte SET_GPS = 23;
    byte GET_PBKDF2 = 24;
    byte GET_FILE_SHA_HEADER = 25;
    byte GET_PBKDF2_GROUP = 26;
    byte GET_GROUP_PRIKEY = 27;
    //---------------------------------------

    // AUTOBACKUP(1) AUTOBACKUP EVENT
    byte FILE = 1;
    byte DIRECTORY = 2;
    //---------------------------------------

    // SIGN UP
    byte DUPLICATION_CHECK_FUNCTION =1;
    byte SIGN_UP_FUNCTION = 2;

    byte EMPTY_ID = 1;
    byte EXIST_ID = 2;
	/*
	 * Order
	 * 1. Event Code, Target + File or directory
	 * 2. File or Directory
	 * 3. Name
	 * 4. FileSize
	 *
	 * */

	/*
	 * Server -> Client event rule
	 * event size = 1024;
	 * event[0] ~ event[2] is event code
	 * event kind
	 * 001 = new group code
	 */
}
