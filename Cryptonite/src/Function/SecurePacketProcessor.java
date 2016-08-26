package Function;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import Crypto.*;

public class SecurePacketProcessor extends PacketProcessor {
	
	private Crypto crypto = null;

	public SecurePacketProcessor(Object channel, boolean blocking) {
		super(channel, blocking);
		// TODO Auto-generated constructor stub
	}
	
	public void init(SecretKey key) {
		crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, key));
	}
	
	@Override
	public PacketProcessor setAllocate(long size) {
		// TODO Auto-generated method stub
		
		return super.setAllocate(Crypto.calc((int)size));
	}

	@Override
	public PacketProcessor setPacket(byte[] packet, int size) {
		byte[] byteArr = new byte[Crypto.calc(size)];
		
		for(int i=0; i<packet.length; i++){
			byteArr[i] = packet[i];
		}
		crypto.endecription(byteArr);
		
		return super.setPacket(byteArr);
	}

	@Override
	public PacketProcessor setPacket(byte[] packet) {
		byte[] byteArr = crypto.endecription(packet);
	
		return super.setPacket(byteArr);
	}

}
