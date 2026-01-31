package com.study.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.project.DTO.LikePostDto;
import com.study.project.model.Likeyo;
import com.study.project.model.Member;
import com.study.project.model.Post;
import com.study.project.repository.CommentRepo;
import com.study.project.repository.LikeyoRepo;
import com.study.project.repository.PostRepo;

@Service
public class LikeyoService {
	
	@Autowired
	private LikeyoRepo likeyoRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	public String likeToggle(int postNo, int memberNo) {
		
		Optional<Likeyo> like = likeyoRepo.findByPostNoAndMemberNo(postNo, memberNo);
		
		if(like.isPresent()) {
			Likeyo l = like.get();
			l.setStatus(l.getStatus().equals("Y") ? "N" : "Y");
			likeyoRepo.save(l);
			return l.getStatus();
		} else {
			Likeyo tmp = new Likeyo();
			tmp.setMemberNo(memberNo);
			tmp.setPostNo(postNo);
			tmp.setStatus("Y");
			likeyoRepo.save(tmp);
			return "Y";
		}
	}
	
	public Page<Likeyo> getMyLikes(int memberNo, int page){
		Pageable pageable = PageRequest.of(page, 10, Sort.by("likeyoNo").descending());
		Page<Likeyo> likedPage = likeyoRepo.findByMemberNo(memberNo, pageable);
		
		return likedPage;
	}
	
	public List<LikePostDto> likesToDto(Page<Likeyo> likedPage, int memberNo){
		List<Integer> postNos = likedPage.getContent().stream().map(Likeyo::getPostNo).toList();
		
		List<Post> tmp = postRepo.findAllById(postNos);
		List<LikePostDto> likedPosts = new ArrayList<>();
		
		for(Post post : tmp) {
			LikePostDto dto = new LikePostDto();
			Member member = post.getMember();
			
			Optional<Likeyo> liked = likeyoRepo.findByPostNoAndMemberNo(post.getPostNo(), memberNo);
			
			dto.setLikeyoNo(liked.get().getLikeyoNo());
			dto.setPostNo(post.getPostNo());	
			dto.setMemberNo(member.getMemberNo());
			dto.setMemberNickname(member.getMemberNickname());
			dto.setPostTitle(post.getPostTitle());
			dto.setPostView(post.getPostView());
			dto.setPostDate(post.getPostDate());
			dto.setCommentCnt(commentRepo.countByMemberNo(member.getMemberNo()));
			
			likedPosts.add(dto);
		}
		
		return likedPosts;
	}
	
	@Transactional
	public void cancelLikes(List<Long> likeyoNos) {
		likeyoRepo.deleteAllById(likeyoNos);
	}

}
