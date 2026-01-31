package com.study.project.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.study.project.model.Member;

public class MyUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private final Member member;
	
	public MyUserDetails(Member member) {
		this.member = member;
	}
	
	public Member getMember() {
		return member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한을 문자열로 가져옴
		return List.of(() -> "ROLE_" + member.getMemberRole());
	}

	@Override
	public String getPassword() {
		return member.getMemberPw();
	}

	@Override
	public String getUsername() {
		return member.getMemberId();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true; // 만료되지 않음
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true; // 잠기지 않음
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 자격증명 만료되지 않음
	}
	
	@Override
	public boolean isEnabled() {
		return true; // 계정 활성화
	}
	
	

}
