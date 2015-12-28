package com.zeshanaslam.aycserver.utils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.zeshanaslam.aycserver.Main;

public class Encryption {

	private Key key = null;

	public Encryption() {
		try {
			key = new SecretKeySpec(Main.configLoader.configValues.get("enKey").getBytes("UTF-8"), "AES");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String value) 
	{
		String encryptedValue64 = null;

		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte [] encryptedByteValue = cipher.doFinal(value.getBytes("UTF-8"));

			encryptedValue64 = Base64.getEncoder().encodeToString(encryptedByteValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return encryptedValue64;
	}

	public boolean checkPassword(String input, String data) {
		return decrypt(input).equals(data);
	}

	private String decrypt(String value)
	{
		String decryptedValue = null;

		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte [] decryptedValue64 = Base64.getDecoder().decode(value);
			byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);

			decryptedValue = new String(decryptedByteValue,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return decryptedValue;   
	}
}
