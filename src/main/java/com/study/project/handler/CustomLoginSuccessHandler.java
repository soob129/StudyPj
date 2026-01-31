package com.study.project.handler;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		String redirectURL = "/show/Main";
		
		for(GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();
			if(role.equals("ROLE_ADMIN")) {
				redirectURL = "/admin/main";
				break;
			}
		}
		
		response.sendRedirect(redirectURL);
	}

}
