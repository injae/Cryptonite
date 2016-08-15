package Server;

import java.io.IOException;

import org.omg.CORBA.ACTIVITY_COMPLETED;

public class Server_Logout extends Server_Funtion{

	Server_Client_Activity _activity;
	Server_Client_Manager _manager;
	@Override
	public void Checker(byte[] packet, Server_Client_Activity activity) {
		// TODO 자동 생성된 메소드 스텁
		_activity=activity;
		_packetMaxCount = 1;
		_manager=Server_Client_Manager.getInstance();
		_manager.requestManage(activity.getClientCode());
	}

	@Override
	public void running() throws IOException {
		// TODO 자동 생성된 메소드 스텁
		Server_Client_Manager.getInstance().logOut(_activity.getClientCode());
		System.out.println("로그아웃");
	}
	

}
