package com.zeshanaslam.aycserver.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.zeshanaslam.aycserver.Main;

public class Encryption {

	public String encrypt(String data) {
		String encrpyedString = null;
		Cipher desCipher;
		byte[] decodedKey = Base64.getDecoder().decode(Main.configLoader.getString("encryptionKey"));
		
		try {
			desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			SecretKey desKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES"); 
			
			desCipher.init(Cipher.ENCRYPT_MODE, desKey);

			encrpyedString = new String(desCipher.doFinal(data.getBytes()));
		} catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		return encrpyedString;
	}
	
	public String decrypt(String data) {
		String decryptedString = null;
		Cipher desCipher;
		byte[] decodedKey = Base64.getDecoder().decode(Main.configLoader.getString("encryptionKey"));
		
		try {
			desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			SecretKey desKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES"); 
			
			desCipher.init(Cipher.DECRYPT_MODE, desKey);

			decryptedString = new String(desCipher.doFinal(data.getBytes()));
		} catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		return decryptedString;
	}
}
