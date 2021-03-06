package com.example;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(classes=ShareEventAppApplication.class)
public class SpringSecurityTest {


	@Autowired
	MockMvc mockMvc;

	@Test
	public void ログイン成功後のリダイレクト先と権限が正しいか() throws Exception{
		//登録済
	    mockMvc.perform(
			formLogin().user("a").password("a"))
			.andExpect(status().isFound()).andExpect(redirectedUrl("/"))
			.andExpect(authenticated().withRoles("USER"));
	    //未登録
	    /*
		mockMvc.perform(
			formLogin().user("b").password("b"))
			.andExpect(status().isFound()).andExpect(redirectedUrl("/login_error"))
			.andExpect(authenticated().withRoles("USER"));
			*/
    }

	@Test
	@WithMockUser("a")

	public void ログアウト成功後のリダイレクト先と認証情報が破棄されているか() throws Exception{

		mockMvc.perform(
				logout())
				.andExpect(status().isFound()).andExpect(redirectedUrl("/"))
				.andExpect(unauthenticated());
	}

	@Test
    @WithMockUser("a")

	public void ユーザー情報() throws Exception {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String message = "class = " + auth.getClass() + "\n" +
		                      "name = " + auth.getName() + "\n" +
	                          "credentials = " + auth.getCredentials() + "\n" +
	                          "authorities = " + auth.getAuthorities() + "\n" +
	                          "principal = " + auth.getPrincipal() + "\n" +
	                          "details = " + auth.getDetails();

	        System.out.println(message);
    }

    }


