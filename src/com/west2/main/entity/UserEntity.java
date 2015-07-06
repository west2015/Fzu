package com.west2.main.entity;

public class UserEntity {
	private String username;
	private String password;
	private String realname;
	private String loginId;

	public UserEntity(){
		
	}
	
	public UserEntity(String username,String password,String realname){
		this.username = username;
		this.password = password;
		this.realname = realname;
	}
	
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
}
