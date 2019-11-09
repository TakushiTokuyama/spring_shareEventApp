package com.example.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.logic.FindLoginUserLogic;
import com.example.loginUser.Account;
import com.example.loginUser.RegisterUserService;

@Controller
public class AuthController {

	@Autowired
	RegisterUserService resisterUserService;

	@GetMapping("/")
	public String index(Model model, Principal principal) {

		FindLoginUserLogic findLoginUserLogic = new FindLoginUserLogic();

		String loginUser = findLoginUserLogic.FindLoginUser(model, principal);

		if (principal != null) {

			model.addAttribute("loginUser", loginUser);

			return "index";

		}
		return "index";
	}

	@RequestMapping("/login")
	public String login(Model model) {

		return "login";
	}

	@RequestMapping("/login_error")
	public String login_error(Model model) {

		model.addAttribute("loginError", true);

		return "login";
	}

	@GetMapping("/signup")
	public String signup(Model model) {

		return "signup";
	}

	@PostMapping("/signup")
	public String loginForm(Model model, @ModelAttribute("username") String username,
			@ModelAttribute("password") String password) throws Exception {

		Account account = new Account();

		account.setUsername(username);

		account.setPassword(password);

		String message = resisterUserService.registerUser(account);

		model.addAttribute("message", message);

		return "login";
	}

}