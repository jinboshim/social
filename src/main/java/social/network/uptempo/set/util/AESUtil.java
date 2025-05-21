package social.network.uptempo.set.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	private static final byte[] key = "ayeonglucasseola".getBytes(StandardCharsets.UTF_8);
	private static final String ALGORITHM = "AES";

//	public static AESUtil instance = new AESUtil();

	public static String encrypt(String plainText) {
		try {
			return Base64.getEncoder().encodeToString(encrypt(plainText.getBytes(StandardCharsets.UTF_8)));
		}
		catch (Exception e) {
			return "1"; 
		}
	}

	public static String decrypt(String encText) {
		try {
			return new String(decrypt(Base64.getDecoder().decode(encText)), StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			return "1"; 
		}
	}
	
	private static byte[] encrypt(byte[] plainText) throws Exception {
	    SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
	    Cipher cipher = Cipher.getInstance(ALGORITHM);
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	
	    return cipher.doFinal(plainText);
	}
	
	private static byte[] decrypt(byte[] cipherText) throws Exception {
	    SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
	    Cipher cipher = Cipher.getInstance(ALGORITHM);
	    cipher.init(Cipher.DECRYPT_MODE, secretKey);
	
	    return cipher.doFinal(cipherText);
	}
}
