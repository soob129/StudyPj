package com.study.project.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.study.project.model.Member;
import com.study.project.repository.MemberRepo;
import com.study.project.security.MyUserDetails;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	private final MemberRepo memberRepo;
	
	public MyUserDetailsService(MemberRepo memberRepo) {
		this.memberRepo = memberRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepo.findByMemberId(username);
		if(member == null) {
			throw new UsernameNotFoundException("User not found");
		}

				
		return new MyUserDetails(member);
	}
}
