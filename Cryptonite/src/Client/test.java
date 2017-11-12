package Client;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import Crypto.Crypto_Factory;
import Crypto.KeyReposit;

public class test {
	
	public static void main(String[] argv)
	{
		try{
			
			String q = "C5lUH+jiwyiyUhzRxwdcelmQI6r3eCp6d9356qvpySVXO8Od74Qndc/GnJ6uiSX4Gn6Y/Smv8w1yLak8u4H5Mg9T6LvEH+QwVx8DFfTIyx/4iI04oqv8GSpQm3jY/zFhC6g+p+dnqRu00RIYJ0EDtBeoBsJus95x0O27C1OTLSI=";
			byte[] w = Base64.getDecoder().decode(q);
			
			String sk = "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAKbGXfgDm4stDOz6KBP9ul8ZeFiD/Kwc4aUqz438IImcjLy/afTQE8lA+tXnN97dWAJw3JYF3l+cPOvobc+UFGPB9skJqsdnUAV9vw+nTLbYRHEW7Ld1ChyWfmQd5eJnMAZkzB4kaMabLkUQ7BrlJVxlrBjFIbpuzEL7Izmug4o7AgMBAAECgYBEsA6jx1iYycU5FQ7MrQPFh0f5rOK0QHDvBeLWJ7F+++s7Edsh6VE84nZtAv4P3DoTR0iSwXgFCOROhTw08lgy07Iow4bxnOLHfnvb4GsPzM/vbBkD9QcXX2e67CbQ/IsYWZp6OwVbpbDpwp9AXyO9FwC/wZQsgmA1DOVKu6Q8AQJBANXcU2xEm5m1zZONP6sFQffZaMlLO1veTLKPge7HvPe7L/cKIU/XNeYYu/kgil1PfYjnJ4IX+HCDJdjpdP4F+aECQQDHoupBcTMz1uBYB2BVcQia63KQn6nLEUpKOGI5Hd6wSk00EQMm0f7FqEgcafY3rFPCO9lqTcJi7RVsB3eXnQ5bAj8P30pRvsXNorCfQtx21O0QeBJO3kaJivYmSoBaOHjN6halPxs50b8uVKee1ctvIXcvsfg8r4rAzvsAHlOQhuECQBCv13Dc63C710jEZRL/Pb7lS1A3aFPnABSwHdW0X3bQ3x8pBOBr0SXoaQ6m9MO2jdHAeu3dzg/CzXWYMjDkGd0CQDoWQf3JTFGu8/OiPWubITsYn7+cnlulYi4o6MMq2nDBzA1mjjqW4KEZEUahpc3PsyOqNqs+l9B/sgxSCEki0Nw=";
			byte[] e = Base64.getDecoder().decode(sk);
			
			EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(e);
	        KeyFactory generator = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = generator.generatePrivate(privateKeySpec);
			
			byte[] t = Crypto_Factory.create("RSA1024", Cipher.DECRYPT_MODE, privateKey).doFinal(w);
			
			String y = Base64.getEncoder().encodeToString(t);
			System.out.println(y);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
