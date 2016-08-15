package Server;

import Function.PacketRule;

public class Server_Function_Factory implements PacketRule 
{
	public static Server_Funtion create(byte mode)
	{
		switch(mode)
		{			
		case AUTOBACKUP:
			return new Server_AutoBackup();
		case LOGIN:
			return new Server_Login();
		case FILE_SHARE_RECEIVE:
			return new Server_FileShare_Receive();
		case FILE_SHARE_SEND:
			return new Server_FileShare_Send();
		case SIGN_UP:
			return new Server_SignUp();
		case MAKE_OTP:
			return new Server_MakeOTP();
		case LOGOUT:
			return new Server_Logout();
		case MAKE_GROUP:
			return new Server_Make_Group();
		default:
			System.out.println("Server Funtion Factory: "+ mode);
			return	new Server_ImDie();
		}
	}
}
