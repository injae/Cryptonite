package Client;

import java.io.IOException;
import java.nio.ByteBuffer;

import Function.PacketRule;

public class Client_Make_Group implements PacketRule
{
	Client_Server_Connector _csc;
	
	public Client_Make_Group() 
	{
		_csc = Client_Server_Connector.getInstance();
	}
	
	public void make(String[] groupmember, String gpname, boolean usegps, double latitude, double longitude, double Radius)
	{
		try 
		{
			byte[] event = new byte[1024];
            byte[] lat = new byte[8];
            byte[] lng = new byte[8];
            byte[] radius = new byte[8];
            
			event[0] = MAKE_GROUP;
			event[1] = (byte)(1 + groupmember.length + 3 + 1);
            event[2] = usegps ? (byte)1 : (byte)0 ;
            
			_csc.send.setPacket(event).write();
			
			for(int i = 0; i < groupmember.length; i++)
			{
				_csc.send.setPacket(groupmember[i].getBytes(), 500).write();
			}

             if (usegps) {   //use gps
                 lat = ByteBuffer.wrap(lat).putDouble(latitude).array();
                 lng = ByteBuffer.wrap(lng).putDouble(longitude).array();
                 radius = ByteBuffer.wrap(radius).putDouble(Radius).array();
             } else {                    //not use gps
                 lat = ByteBuffer.wrap(lat).putDouble(0).array();
                 lng = ByteBuffer.wrap(lng).putDouble(0).array();
                 radius = ByteBuffer.wrap(radius).putDouble(0).array();
             }

             _csc.send.setPacket(lat, 8).write();
             _csc.send.setPacket(lng, 8).write();
             _csc.send.setPacket(radius, 8).write();

             _csc.send.setPacket(gpname.getBytes(),500).write();

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
