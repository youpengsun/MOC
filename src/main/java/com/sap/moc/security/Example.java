package com.sap.moc.security;

import com.sap.moc.security.PassProtect;
import com.sap.moc.security.PassGenerate;

public class Example {
	public static void main(String[] args) throws Exception {
		String[] passwordSaltHash = PassGenerate.generatePasswordSaltHash();
		String password = passwordSaltHash[0];
		String salt = passwordSaltHash[1];
		String hash = passwordSaltHash[2];

		System.out.println("生成的密码 Password: " + password);
		System.out.println("哈西效验值 Hash: " + hash);
		System.out.println("盐 Salt: " + salt);
		System.out.println();

		String exampleWrongPassword = "ABCDEFG";
		System.out.println("Verifying password: " + exampleWrongPassword);
		System.out.println("Correct: " + PassProtect.verifyPasswordSaltHash(exampleWrongPassword, salt, hash));

		System.out.println();

		System.out.println("Verifying password: " + password);
		System.out.println("Correct: " + PassProtect.verifyPasswordSaltHash(password, salt, hash));
		
		System.out.println("111111+111111=>:  " + PassProtect.hashPasswordSalt("111111", "111111"));
	}
}
