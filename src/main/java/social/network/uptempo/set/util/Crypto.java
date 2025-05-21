package social.network.uptempo.set.util;

import java.security.MessageDigest;

import com.oreilly.servlet.Base64Encoder;

public class Crypto {

	private Crypto() {
	    throw new IllegalAccessError("Crypto class do not access");
	}
	
	public static String sha1(String plain) {
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(plain.getBytes("utf-8"));
			return Base64Encoder.encode(crypt.digest());
		} catch (Exception e) {
			return plain;
		}
	}
	
	public static String sha256(String plain) {
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-256");
			crypt.reset();
			crypt.update(plain.getBytes("utf-8"));
			return Base64Encoder.encode(crypt.digest());
		} catch (Exception e) {
			return plain;
		}
	}
}
