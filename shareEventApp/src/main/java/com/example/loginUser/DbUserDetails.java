package com.example.loginUser;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class DbUserDetails extends User {

	private final Account account;

	public DbUserDetails(Account account,
			Collection<GrantedAuthority> authorities) {

		super(account.getUsername(), account.getPassword(),
				true, true, true, true, authorities);

		this.account = account;
	}

	public Account getAcount() {
		return account;
	}

}
