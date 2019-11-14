package com.example.loginUser;

import javax.validation.constraints.Size;

public class Account {

	@Size(min = 0, max = 10, message = "Username:0-10文字")
	private String username;
	@Size(min = 0, max = 20, message = "Password:0-20文字")
	private String password;

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

}