package com.example.loginUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mapper.UserMapper;

@Service
@Transactional
public class RegisterUserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Transactional
	public String registerUser(Account account) throws Exception {

		try {

			Account ac = userMapper.findUser(account);

			if (ac != null) {

				return "既にUsernameが登録されています";
			}

			account.setPassword(passwordEncoder.encode(account.getPassword()));

			userMapper.saveUser(account);

			return "登録できました";

		} catch (Exception ex) {

			System.out.println(ex);

		}
		return "DBError";
	}
}
