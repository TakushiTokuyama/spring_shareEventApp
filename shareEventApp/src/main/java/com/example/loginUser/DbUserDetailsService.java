package com.example.loginUser;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mapper.UserMapper;

@Service
public class DbUserDetailsService implements UserDetailsService {

	@Autowired
	UserMapper userMapper;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		//DBからユーザ情報を取得。
		Account account = Optional.ofNullable(userMapper.findUsername(username))
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));

		return new DbUserDetails(account, getAuthorities(account));
	}

	private Collection<GrantedAuthority> getAuthorities(Account account) {
		//権限
		return AuthorityUtils.createAuthorityList("ROLE_USER");
	}

}
