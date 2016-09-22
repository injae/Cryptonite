package Function;

import java.nio.ByteBuffer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.*;

public class SecurePacketProcessor extends PacketProcessor {
	
	private Crypto _crypto = null;
	private SecretKey _key = null;

	public SecurePacketProcessor(Object channel, boolean blocking) {
		super(channel, blocking);
		_key = new SecretKeySpec("unvalidfirstkeyspecunalidfirstke".getBytes(),"AES");
		_crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, _key));
		// TODO Auto-generated constructor stub
	}
	
	public void init(SecretKey key) {
		_key = key;
	}
	
	@Override
	public PacketProcessor setAllocate(long size) 
	{
		return super.setAllocate(size);
	}

	@Override
	public PacketProcessor setPacket(byte[] packet, int size) {
		_crypto.init(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, _key));
		byte[] array = new byte[size];
		for(int i =0; i  < packet.length; i++)
		{
			array[i] = packet[i];
		}
		return super.setPacket(_crypto.endecription(array));
	}

	@Override
	public byte[] getByte() {
		// TODO 자동 생성된 메소드 스텁
		_crypto.init(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, _key));
		return _crypto.endecription(super.getByte());
	}
	
	@Override
    public ByteBuffer getByteBuf() {
        _crypto.init(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, _key));
        return _crypto.endecription(super.getByteBuf());
    }

	@Override
	public PacketProcessor setPacket(byte[] packet) {
		_crypto.init(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, _key));
		byte[] byteArr = _crypto.endecription(packet);

		return super.setPacket(byteArr);
	}

}
