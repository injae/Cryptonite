package Client;

import java.io.IOException;

import Function.Function;
import Function.PacketRule;

public class Client_GetGPS implements PacketRule {

	private Client_Server_Connector csc = null;
	private double _xpos = 0;
	private double _ypos = 0;

	public Client_GetGPS() {
		// Server Connect
		csc = Client_Server_Connector.getInstance();
	}
	
	public void getGPS(){
		byte[] event = new byte[1024];
		byte[] xposBytes = null;
		byte[] yposBytes = null;
		
        event[0] = GET_GPS;
        event[1] = 2;
        try {
			csc.send.setPacket(event).write();
			
			xposBytes = csc.receive.setAllocate(8).read().getByte();
			yposBytes = csc.receive.setAllocate(8).read().getByte();
			
			_xpos = Function.byteArrayToDouble(xposBytes);
			_ypos = Function.byteArrayToDouble(yposBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double get_xpos(){
		return this._xpos;
	}
	
	public double get_ypos(){
		return this._ypos;
	}

}
