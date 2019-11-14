package com.example.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.logic.FindLoginUser;
import com.example.loginUser.Account;
import com.example.loginUser.RegisterUserService;

@Controller
public class AuthController {

	@Autowired
	RegisterUserService resisterUserService;

	@GetMapping("/")
	public String index(Model model, Principal principal) {

		FindLoginUser findLoginUser = new FindLoginUser();

		if (principal != null) {
			model.addAttribute("loginUser", findLoginUser.FindLoginUser(model, principal));
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
	public String signup(Model model,@ModelAttribute("formModel")Account account) {

		return "signup";
	}

	@PostMapping("/signup")
	public String loginForm(Model model,@ModelAttribute("formModel")@Validated Account account,BindingResult result){

		if (result.hasErrors()) {
			return "signup";
		}
		account.setUsername(account.getUsername());
		account.setPassword(account.getPassword());

		return resisterUserService.registerUser(account,model);

	}
}