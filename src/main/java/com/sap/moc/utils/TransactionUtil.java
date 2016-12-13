package com.sap.moc.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * generate transaction code
 * 
 * @author I322244
 *
 */
public class TransactionUtil {
	public static String generateCode() {
		Random random = new Random();
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			byte[] digest2 = digest.digest((format.format(date) + "" + Math.random()).getBytes());
			String transaction = Math.abs(new BigInteger(1, digest2).hashCode()) + "";
			while (transaction.length() != 10) {
				if (transaction.length() > 10) {
					transaction = transaction.substring(transaction.length() - 10);
				} else if (transaction.length() < 10) {
					transaction += random.nextInt(10);
				}
			}
			for (int i = 0; i < 10; i++) {
				int pos = random.nextInt(10);
				transaction = transaction.substring(0, pos) + random.nextInt(10) + transaction.substring(pos + 1);
			}
			if (transaction.charAt(0) == '0') {
				int number = random.nextInt(8) + 1;
				transaction = number + transaction.substring(1);
			}
			return transaction;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
