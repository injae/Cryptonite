package Server;

import Function.PacketRule;

/*
 * @author In Jae Lee
 * 
 */

public class Server_Function_Factory implements PacketRule 
{
	public static Server_Funtion create(byte mode, Server_Client_Activity activity) 
	{
		switch(mode)
		{			
		case AUTOBACKUP:
			return new Server_AutoBackup(activity);
		case LOGIN:
			return new Server_Login(activity);
		case FILE_SHARE_RECEIVE:
			return new Server_FileShare_Receive(activity);
		case FILE_SHARE_SEND:
			return new Server_FileShare_Send(activity);
		case SIGN_UP:
			return new Server_SignUp(activity);
		case MAKE_OTP:
			return new Server_MakeOTP(activity);
		case LOGOUT:
			return new Server_Logout(activity);
		case MAKE_GROUP:
			return new Server_Make_Group(activity);
		case FILE_LIST_REQUEST:
			return new Server_File_ListSender(activity);
		case FILE_DOWNLOAD:
			return new Server_File_Download(activity);
		case EVENT:
			return new Server_Send_Event(activity);
		default:
			System.out.println("Server Funtion Factory: "+ mode);
			return	new Server_ImDie(activity);
		}
	}
}
