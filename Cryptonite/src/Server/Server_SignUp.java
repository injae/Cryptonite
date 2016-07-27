package Server;

public class Server_SignUp extends Server_Funtion 
{
	@Override
	public void Checker(byte[] packet) {
		// TODO 자동 생성된 메소드 스텁	
		_packetMaxCount =packet[1];
		System.out.println(_packetMaxCount);
	}

	@Override
	public void running(Server_Client_Activity activity) {
		// TODO 자동 생성된 메소드 스텁
		
	}
}
