package com.zeshanaslam.aycserver.utils;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class Encryption {

	public String encrypt(String data) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

		return passwordEncryptor.encryptPassword(data);
	}

	public boolean checkPassword(String input, String data) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

		return passwordEncryptor.checkPassword(input, data);
	}
}
