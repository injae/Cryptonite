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
		case FILE_UPLOAD:
			return new Server_File_Upload(activity);
		case EVENT:
			return new Server_Send_Event(activity);
		case DELETE_GROUP:
			return new Server_Delete_Group(activity);
		case GROUP_INVITE:
			return new Server_Group_Invite(activity);
		case KEY_EXCHANGE:
			return new Server_KeyExchange(activity);
		case GROUP_WITHDRAWAL:
			return new Server_Group_Withdrawal(activity);
		case GROUP_SEARCH:
			return new Server_Group_Search(activity);
		case SHOW_GROUP:
			return new Server_Show_Group(activity);
		case FIND_CAPTAIN:
			return new Server_Find_Captain(activity);
		case GET_GPS:
			return new Server_GetGPS(activity);
		case GET_GROUP_KEY:
			return new Server_Get_GroupKey(activity);
		case FOLDER_LIST:
			return new Server_Folder_List(activity);
		default:
			System.out.println("Server Funtion Factory: "+ mode);
			return	new Server_ImDie(activity);
		}
	}
}
