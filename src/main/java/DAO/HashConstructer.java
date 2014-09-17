package DAO;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
public class HashConstructer {
	private static final int iterations = 10 * 1024;
	private static final int saltLen = 32;
	private static final int desiredKeyLen = 256;

	/*
	 * This function takes password , generate salt , append salt with password
	 * and finally gets the hash
	 */
	public static String getSaltedHash(String password) {
		try {
			byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(
					saltLen);
			String saltString = new sun.misc.BASE64Encoder().encode(salt);
			return saltString + "$" + hash(password, salt);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/*
	 * Actual Hash Takes place in the below function
	 */

	private static String hash(String password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(),
				salt, iterations, desiredKeyLen));
		return new sun.misc.BASE64Encoder().encode(key.getEncoded());
	}

	/*
	 * This function checks the password and stored hash.Return true is yes else
	 * false.
	 */

	public static boolean check(String password, String stored)
			throws Exception {
		String[] saltAndPass = stored.split("\\$");
		if (saltAndPass.length != 2)
			return false;

		BASE64Decoder decoder = new BASE64Decoder();
		byte[] passwordByteArray = decoder.decodeBuffer(saltAndPass[0]);
		String hashOfInput = hash(password, passwordByteArray);

		return hashOfInput.equals(saltAndPass[1]);
	}

}
