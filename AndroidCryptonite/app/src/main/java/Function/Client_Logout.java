package Function;

import java.io.IOException;

import Function.Client_Server_Connector;
import Function.PacketRule;

public class Client_Logout extends Thread implements PacketRule{

    private Client_Server_Connector _csc;

    public Client_Logout(){
        this.start();
    }

    @Override
    public void run() {
        logout();
    }

    public void logout(){
        if(Client_Server_Connector.isConnected()) {
            Client_Server_Connector.getInstance().receive.close();
            Client_Server_Connector.getInstance().send.close();
            try {
                Client_Server_Connector.getInstance()._channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("연결상태" + Client_Server_Connector.getInstance()._channel.isConnected());
        }
    }
}
