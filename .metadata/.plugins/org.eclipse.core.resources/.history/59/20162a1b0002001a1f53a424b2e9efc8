package com.example.logic;

import java.security.Principal;

import org.springframework.ui.Model;

public class FindLoginUser {

	private String loginUser;

	public String FindLoginUser(Model model, Principal principal) {

		if(principal != null) {

			this.loginUser = principal.getName();

			return loginUser;

		}

		return "ログインユーザーはいません";

	}

}
