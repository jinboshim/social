package social.network.uptempo.set.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaUtil {
	
	public static String sha256(String text) {
		String returnStr = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(text.getBytes("utf8"));
			returnStr = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}
	

    public static String sha512(String text) throws NoSuchAlgorithmException {
    	String returnStr = null;
    	try {
    	    MessageDigest digest = MessageDigest.getInstance("SHA-512");
    	    digest.reset();
    	    digest.update(text.getBytes("utf8"));
    	    returnStr = String.format("%0128x", new BigInteger(1, digest.digest()));
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return returnStr;
    }
	
	
	
	
}
