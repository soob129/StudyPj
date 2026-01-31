package com.study.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.study.project.handler.CustomLoginSuccessHandler;
import com.study.project.service.MyUserDetailsService;

@Configuration
public class SecurityConfig {
	
	@Autowired
    private MyUserDetailsService userDetailsService;
	
	@Autowired
	private CustomLoginSuccessHandler customLoginSuccessHandler;
	
	public SecurityConfig(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
	
	 @Bean
	 public static BCryptPasswordEncoder bCryptPasswordEncoder() {
		 return new BCryptPasswordEncoder();
	}
	 
	 @Bean
	 public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		 AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		 authBuilder.userDetailsService(userDetailsService)
		               .passwordEncoder(bCryptPasswordEncoder());
		 return authBuilder.build();
	 }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf
					.ignoringRequestMatchers("/member/recruitForm")
			)
			.authenticationManager(authenticationManager(http))
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/", "/show/Main", "/user/**", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll() // 인증 생략
					.anyRequest().authenticated() // 인증 필요
					
			)
			.formLogin(form -> form
					.loginPage("/user/login")	// 커스텀 로그인 페이지
					.loginProcessingUrl("/user/checkUser")		// 로그인 처리 url (폼 action url)
					.successHandler(customLoginSuccessHandler) // 로그인 성공 후 이동할 url
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/member/logout")
					.logoutSuccessUrl("/show/Main")
					.permitAll()
			)
			
			.sessionManagement((auth) -> auth
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			)
					
			.sessionManagement(session -> session
					 .maximumSessions(1)
					 .maxSessionsPreventsLogin(true)
			);
		
		
		return http.build();
			
	}
}
