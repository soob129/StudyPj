package com.study.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.project.model.Member;

@Repository
public interface MemberRepo extends JpaRepository<Member, Integer> {
	
	public Member findByMemberId(String username);

	public boolean existsByMemberNickname(String nickname);
	
	public Member findByMemberNameAndMemberEmail(String name, String email);
	
	public Member findByMemberIdAndMemberNameAndMemberEmail(String id, String name, String email);
	
	public Member findByMemberNo(int memberNo);
}
