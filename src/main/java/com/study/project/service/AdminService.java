package com.study.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.project.DTO.MemberDetailAdminDto;
import com.study.project.DTO.MemberListAdminDto;
import com.study.project.DTO.PostCommentDto;
import com.study.project.DTO.PostDetailAdminDto;
import com.study.project.DTO.PostListDto;
import com.study.project.model.Comment;
import com.study.project.model.Member;
import com.study.project.model.MemberStatus;
import com.study.project.model.Post;
import com.study.project.model.PostStatus;
import com.study.project.repository.CommentRepo;
import com.study.project.repository.MemberRepo;
import com.study.project.repository.PostRepo;

@Service
public class AdminService {
	
	@Autowired
	private MemberRepo memberRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	public long getMemberCnt() {
		return memberRepo.count();
	}
	
	public long getPostCnt() {
		return postRepo.count();
	}
	
	public long getCommentCnt() {
		return commentRepo.count();
	}
	
	public Map<Integer, Long> getMonthlyPosts(){
		List<Object[]> result = postRepo.countPostsByMonth();
		Map<Integer, Long> map = new HashMap<>();
		for(Object[] obj : result) {
			Integer month = ((Number)obj[0]).intValue();
			Long cnt = ((Number)obj[1]).longValue();
			map.put(month, cnt);
		}
		return map;
	}
	
	public List<MemberListAdminDto> getAllMembers(){
		List<Member> members = memberRepo.findAll();
		
		List<MemberListAdminDto> memberList = members.stream().map(m -> {
			MemberListAdminDto dto = new MemberListAdminDto();
			dto.setMemberNo(m.getMemberNo());
			dto.setMemberId(m.getMemberId());
			dto.setMemberNickname(m.getMemberNickname());
			dto.setJoinDate(m.getJoinDate());
			dto.setMemberStatus(m.getMemberStatus());
			dto.setMemberRole(m.getMemberRole());
			
			return dto;
		}).collect(Collectors.toList());
		
		return memberList;
	}
	
	public Page<MemberListAdminDto> getMembersPage(int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by("memberNo").ascending());
		
		Page<Member> memberPage = memberRepo.findAll(pageable);
		
		Page<MemberListAdminDto> dtoPage = memberPage.map(m -> {
			MemberListAdminDto dto = new MemberListAdminDto();
			dto.setMemberNo(m.getMemberNo());
			dto.setMemberId(m.getMemberId());
			dto.setMemberNickname(m.getMemberNickname());
			dto.setJoinDate(m.getJoinDate());
			dto.setMemberStatus(m.getMemberStatus());
			dto.setMemberRole(m.getMemberRole());
			
			return dto;
		});
		
		return dtoPage;
	}
	
	public Page<PostListDto> getPostsPage(int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by("postNo").ascending()); 
		
		Page<Post> postPage =postRepo.findAll(pageable);
		
		Page<PostListDto> dtoPage = postPage.map(p -> {
			PostListDto dto = new PostListDto();
			dto.setPostNo(p.getPostNo());
			dto.setMemberNo(p.getMember().getMemberNo());
			dto.setPostCategory(p.getPostCategory());
			dto.setPostTitle(p.getPostTitle());
			dto.setMemberNickname(p.getMember().getMemberNickname());
			dto.setPostView(p.getPostView());
			dto.setPostDate(p.getPostDate());
			dto.setPostStatus(p.getPostStatus());
			
			int commentCnt = commentRepo.countByPostNo(p.getPostNo());
			dto.setCommentCnt(commentCnt);
			
			return dto;
		});
		
		return dtoPage;
	}
	
	public MemberDetailAdminDto getMemberDetail(int memberNo) {
		MemberDetailAdminDto dto = new MemberDetailAdminDto();
		
		Member member = memberRepo.findByMemberNo(memberNo);
		
		dto.setMemberNo(member.getMemberNo());
		dto.setMemberId(member.getMemberId());
		dto.setMemberNickname(member.getMemberNickname());
		dto.setJoinDate(member.getJoinDate());
		dto.setMemberStatus(member.getMemberStatus());
		dto.setMemberRole(member.getMemberRole());
		
		String email = member.getMemberEmail();
		String[] parts = email.split("@");
		String id = parts[0];
		String domain = parts[1];
		
		int show = 1;
		int maskCnt = Math.max(1, id.length() - show);
		
		String maskedId = id.substring(0, Math.min(show, id.length())) + "*".repeat(maskCnt);
		dto.setMemberEmail(maskedId + "@" + domain);
		
		String phone = member.getMemberPhone();
		String maskedPhone;
		
		if(phone.contains("-")) {
			String[] phones = phone.split("-");
			if(phones.length == 3) {
				maskedPhone = parts[0] + "-****-" + parts[2];
			} else {
				maskedPhone= "****";
			}
		} else if(phone.length() == 11) {
			maskedPhone = phone.substring(0, 3) + "-****-" + phone.substring(7);
		} else {
			maskedPhone = phone.substring(0, 3) + "-****-" + phone.substring(phone.length() - 4);
		}
		
		dto.setMemberPhone(maskedPhone);
		
		int postCnt = postRepo.countByMember_MemberNo(memberNo);
		dto.setPostCnt(postCnt);
		
		int commentCnt = commentRepo.countByMemberNo(memberNo);
		dto.setCommentCnt(commentCnt);
		
		return dto;
	}
	
	@Transactional
	public boolean deleteMember(int memberNo) {
		Member member = memberRepo.findByMemberNo(memberNo);
		
		if(member != null) {
			member.setMemberStatus(MemberStatus.STOP);
			return true;
		}
		
		return false;
	}
	
	@Transactional
	public boolean restoreMember(int memberNo) {
		Member member = memberRepo.findByMemberNo(memberNo);
		
		if(member != null) {
			member.setMemberStatus(MemberStatus.ACTIVE);
			return true;
		}
		
		return false;
	}
	
	public PostDetailAdminDto getPostDetail(int postNo) {
		PostDetailAdminDto dto = new PostDetailAdminDto();
		
		Post post = postRepo.findByPostNo(postNo);
		dto.setPost(post);
		
		List<Comment> commentList = commentRepo.findByPostNoOrderByCommentDateAsc(postNo);
		
		List<PostCommentDto> comments = commentList.stream().map(c -> {
			PostCommentDto commentDto = new PostCommentDto();
			int memberNo = c.getMemberNo();
			
			commentDto.setCommentId(c.getCommentId());
			commentDto.setPostNo(c.getPostNo());
			commentDto.setCommentText(c.getCommentText());
			commentDto.setMemberNo(memberNo);
			commentDto.setCommentDate(c.getCommentDate());
			commentDto.setMemberNickname(memberRepo.findByMemberNo(memberNo).getMemberNickname());
			commentDto.setCommentStatus(c.getCommentStatus());
			
			return commentDto;
			
		}).collect(Collectors.toList());
		dto.setComments(comments);	
		
		return dto;
	}
	
	@Transactional
	public boolean deletePost(int postNo) {
		Post post = postRepo.findByPostNo(postNo);
		
		if(post != null) {
			post.setPostStatus(PostStatus.DELETED);
			return true;
		}
		
		return false;
	}
	
	@Transactional
	public boolean restorePost(int postNo) {
		Post post = postRepo.findByPostNo(postNo);
		
		if(post != null) {
			post.setPostStatus(PostStatus.ACTIVE);
			return true;
		}
		
		return false;
	}
}


