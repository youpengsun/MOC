package com.sap.moc.security;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

public class PassGenerate {
	/**
	 * Generates a password, a salt; calculates corresponding hash.
	 * 生成密码，盐，并计算对应的哈希效验值
	 * 
	 * @return A string array with password, salt, and hash
	 */
	public static String[] generatePasswordSaltHash() {
		String password = generatePassword();
		String[] saltHash = PassProtect.hashPassword(password);
		String salt = saltHash[0];
		String hash = saltHash[1];
		return new String[] { password, salt, hash };
	}

	/**
	 * 密码随机生成器 Generates a random alphanumeric password with 130 bits of
	 * entropy. Uses a cryptographically secure pseudorandom number generator
	 * (SecureRandom).
	 * 
	 * @return string, generated password
	 */
	public static String generatePassword() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	/**
	 * Generates a random 256-bit salt.
	 * 
	 * @return salt
	 */
	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[32];
		random.nextBytes(salt);
		String saltBase64 = DatatypeConverter.printBase64Binary(salt);
		return saltBase64;
	}
}
