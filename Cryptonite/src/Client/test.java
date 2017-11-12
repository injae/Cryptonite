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
			
			String q = "cDpm3T1BdccR9YII14iOW/Gp25qDlCtHCziV3zzGb8yISGrxNSa/+a9nnNb3EuDRr4Oewwlk5WQ2zVRGV2dm04AiNzCwAL0uAa8DGXJnMGI2F+6zpFcjcNIUZP/xXzY7kneRBArC786y/SKpelGOQ8wgxCEsvRsDuMJJ2VWqSHo=";
			byte[] w = Base64.getDecoder().decode(q);
			
			System.out.println(q.length());
			System.out.println(w.length);
			
			String sk = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI0cjCg1heBvtTLVfw1Y4qaPsW4YEkCJES8QJD7WvRymWW5a/tOYqhMBm8q04yuj+DePgYCBjuPwRqRtwHE4EEiHGSecUHAMUI5WBaasppIGpIpob1lw1ddD1rosMbVLaDdqjW69K5Al8Z2latAzfrLLwD7UxsKDF7Ya4UeqWHqxAgMBAAECgYAMGle3FygdYNdkvcUA6w+9g3OPYscl+9uacsL5FMfxjh77hRh4I47qxGeNUkhttMmUUl2kHPiZekFE1xj7XPigbmf3xX1FJQayarS2mC+S9QkgJuyEOq5JdXybSCCxJQ5qveUQuJeQe55Eg7Bv0iDcYzDBDovPOWTENnu/J9y4MQJBAO7uiPUQJ6I+FXsBgHK+zsmuSkYeKifYNrPzCRdiQq8/EUK9aseqS/ec34ENadt1TjoaHiincnmjjf9l/gq4GtUCQQCXMR/W40ERf00GOyb1oMD6jkQjvntsw7CDObA6Yopw0sMhN7ChSDJpI5hjcd8CJ/l+bFJYvP6kNgH531FK69ZtAkEA2oA/tUTRyfhWsoecDNNbzmpaOCdLy+ZZmFTwgnb0nsjhIxSP+wpMsKPAbYdzwCNVp6LM48bF1GFy8RY3rVvSBQJAdDO7ZutvHUWcK4fXH74X0/r4AAjsz+FvjswN2DHYeXJjquokhTD6HbjP7M6eOggDR9l1SOKpTAh+aE/tKQot2QJACtyDt/DPfdGUd2+giHym4kZtiL83xHyTOWfvNFwEiUztfqMGc96xSwsWwNoVLSgbEd1Q7/in17BqVM6tBA8OlA==";
			byte[] e = Base64.getDecoder().decode(sk);
			
			System.out.println(sk.length());
			System.out.println(e.length);
			
			EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(e);
	        KeyFactory generator = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = generator.generatePrivate(privateKeySpec);
			
			byte[] t = Crypto_Factory.create("RSA1024", Cipher.DECRYPT_MODE, privateKey).doFinal(w);
			
			String y = Base64.getEncoder().encodeToString(t);
			System.out.println(y);
			System.out.println(y.length());
			System.out.println(t.length);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
