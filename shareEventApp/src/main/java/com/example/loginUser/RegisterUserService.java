package com.example.loginUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.example.mapper.UserMapper;

@Service
@Transactional
public class RegisterUserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Transactional
	public String registerUser(Account account,Model model) {
			Account ac = userMapper.findUser(account);

			if (ac != null) {
				model.addAttribute("message","既にUsernameが登録されています");
				return "signup";
			}

			account.setPassword(passwordEncoder.encode(account.getPassword()));

			userMapper.saveUser(account);

			model.addAttribute("message","登録できました");

			return "login";
	}
}
