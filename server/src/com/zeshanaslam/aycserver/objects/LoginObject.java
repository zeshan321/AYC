package com.zeshanaslam.aycserver.objects;

public class LoginObject {

	public String password;
	public String videos;
	public boolean admin;
	
	public LoginObject(String password, String videos, boolean admin) {
		this.password = password;
		this.videos = videos;
		this.admin = admin;
	}
}
