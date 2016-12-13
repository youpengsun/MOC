package com.sap.moc.security;

import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

public class PassProtect {

	/**
	 * Takes a password, the salt, and the correct hash. Returns true if the
	 * password is correct. 对 明文密码 ，盐， 和 哈希效验值 进行验证。
	 * 
	 * @param password
	 *            明文密码 the password to be verified
	 * @param salt
	 *            "盐" the password's corresponding salt
	 * @param hash
	 *            正确的哈希效验值 the correct hash of the password
	 * @return a boolean value, true if password is correct 密码是否正确
	 */
	public static Boolean verifyPasswordSaltHash(String password, String salt, String hash) {
		String calculatedHash = hashPasswordSalt(password, salt);
		return calculatedHash.equals(hash);
	}

	/**
	 * Takes a password. Returns an array with the salt and the hash.
	 * 对明文密码进行加盐的哈希处理 （哈希!=加密。 哈希是不可逆的，加密是可逆的。）
	 * 
	 * @param password
	 *            明文密码 the password to be hashed
	 * 
	 * @return array with salt and hash. array[0] == hash; array[1] == salt;
	 *         包含哈希值和盐的数组
	 */
	public static String[] hashPassword(String password) {
		String salt = PassGenerate.generateSalt();
		String hash = hashPasswordSalt(password, salt);
		return new String[] { salt, hash };
	}

	/**
	 * Takes a password and a salt. Returns the salted hash of the password.
	 * Uses PBKDF2-HMAC-SHA256 with 100,000 iterations and 256-bit key length.
	 * 
	 * See https://www.owasp.org/index.php/Password_Storage_Cheat_Sheet
	 * 
	 * @param password
	 *            the password to be hashed
	 * @param salt
	 *            the salt to be hashed together with the password
	 * @return hash
	 */
	public static String hashPasswordSalt(String password, String salt) {
		int ITERATIONS = 100000;

		byte[] passwordBytes = password.getBytes();
		byte[] saltBytes = salt.getBytes();

		// PBKDF2WithHmacSHA256
		PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator(new SHA256Digest());
		generator.init(passwordBytes, saltBytes, ITERATIONS);
		byte[] hash = ((KeyParameter) generator.generateDerivedParameters(256)).getKey();
		String hashBase64 = DatatypeConverter.printBase64Binary(hash);

		return hashBase64;
	}

}
