package com.zeshanaslam.aycserver.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashPassword {
	
	public String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
    
	public boolean checkPassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
}