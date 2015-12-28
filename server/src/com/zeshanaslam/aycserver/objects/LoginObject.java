package com.zeshanaslam.aycserver.objects;

public class LoginObject {

	public String email;
	public String password;
	public String videos;
	public boolean admin;
	
	public LoginObject(String email, String password, String videos, boolean admin) {
		this.email = email;
		this.password = password;
		this.videos = videos;
		this.admin = admin;
	}
}
