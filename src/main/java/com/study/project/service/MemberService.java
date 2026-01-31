package com.study.project.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.project.DTO.MemberDto;
import com.study.project.DTO.MemberInfoDto;
import com.study.project.DTO.MypageDto;

import com.study.project.model.Member;
import com.study.project.repository.CommentRepo;
import com.study.project.repository.LikeyoRepo;
import com.study.project.repository.MemberRepo;
import com.study.project.repository.PostRepo;

@Service
public class MemberService {
	@Autowired
	private MemberRepo memberRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private LikeyoRepo likeyoRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public void registerMember(MemberDto dto) {
		
		dto.validate();
		
		if(memberRepo.findByMemberId(dto.getUserId()) != null) {
			throw new RuntimeException("이미 존재하는 아이디입니다.");
		}
		
		Member member = toEntity(dto);
		memberRepo.save(member);
		
	}
	
	public Member toEntity(MemberDto dto) {
		Member member = new Member();
		
		member.setMemberId(dto.getUserId());
		member.setMemberPw(passwordEncoder.encode(dto.getUserPw()));
		member.setMemberNickname(dto.getUserNickname());
		member.setMemberName(dto.getUserName());
		member.setMemberBirth(dto.getUserBirth());
		member.setMemberGender(dto.getUserGender());
		member.setMemberPhone(dto.getUserPhone());
		member.setMemberEmail(dto.getUserEmail());
		member.setMemberRole("USER");
		member.setJoinDate(LocalDate.now());
		
		return member;
	}
	
	public MemberInfoDto findMemberId(String name, String email) {
		Member member = memberRepo.findByMemberNameAndMemberEmail(name, email);
		
		if(member == null) return null;
		
		MemberInfoDto dto = new MemberInfoDto();
		dto.setMemberId(member.getMemberId());
		dto.setJoinDate(member.getJoinDate());
		
		return dto;
	}
	
	public String findMemberPw(String id, String name, String email) {
		Member member = memberRepo.findByMemberIdAndMemberNameAndMemberEmail(id, name, email);
		String memberId = null;
		
		if(member != null) {
			memberId = member.getMemberId();
		}
		
		return memberId;
	}
	
	public void resetPw(String id, String pw) {
		Member member = memberRepo.findByMemberId(id);
		String newPw = passwordEncoder.encode(pw);
		
		if(member != null) {
			member.setMemberPw(newPw);
			memberRepo.save(member);
		}
	}
	
	public boolean pwCheck(String id, String pw) {
		Member member = memberRepo.findByMemberId(id);
		boolean isPastPw = true;
		
		if(member != null) {
			if(!passwordEncoder.matches(pw, member.getMemberPw())) {
				isPastPw = false;
			}
		}
		return isPastPw;
	}
	
	public MypageDto loadMypage(int memberNo) {
		Member member = memberRepo.findByMemberNo(memberNo);
		
		int postCnt = postRepo.countByMember_MemberNo(memberNo);
		int commentCnt = commentRepo.countByMemberNo(memberNo);
		int likeCnt = likeyoRepo.countByMemberNo(memberNo);
		
		MypageDto mypage = new MypageDto();
		mypage.setNickname(member.getMemberNickname());
		mypage.setPostCnt(postCnt);
		mypage.setCommentCnt(commentCnt);
		mypage.setLikeCnt(likeCnt);
		
		return mypage;
	}

	
	
	
	
}
