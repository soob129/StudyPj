package com.study.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.project.DTO.CommentDto;
import com.study.project.model.Comment;
import com.study.project.model.CommentStatus;
import com.study.project.repository.CommentRepo;
import com.study.project.repository.PostRepo;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private PostRepo postRepo;
	
	public Page<Comment> getMyComments(int memberNo, int page) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("commentDate").descending());
		Page<Comment> commentList = commentRepo.findByMemberNoAndCommentStatus(memberNo, CommentStatus.ACTIVE, pageable);
		
		return commentList;
	}
	
	public List<CommentDto> commentToDto(Page<Comment> commentList, int memberNo){

		List<CommentDto> comments = commentList.stream().map(c -> {
			CommentDto dto = new CommentDto();
			dto.setCommentId(c.getCommentId());
			dto.setPostNo(c.getPostNo());
			dto.setCommentText(c.getCommentText());
			dto.setMemberNo(memberNo);
			dto.setCommentDate(c.getCommentDate());
			dto.setPostTitle(postRepo.findByPostNo(c.getPostNo()).getPostTitle());
			
			return dto;
		}).collect(Collectors.toList());
		
		return comments;
	}
	
	public void submitComment(int memberNo, int postNo, String text) {
		Comment c = new Comment();
		c.setPostNo(postNo);
		c.setMemberNo(memberNo);
		c.setCommentText(text);
		c.setCommentDate(LocalDateTime.now());
		c.setCommentStatus(CommentStatus.ACTIVE);
		
		commentRepo.save(c);
	}
	
	public void commentDelete(int commentId, int memberNo) {
		Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
		
		if(comment.getMemberNo() != memberNo) {
			throw new AccessDeniedException("본인 댓글만 삭제할 수 있습니다.");
		}
		
		comment.setCommentStatus(CommentStatus.DELETED);
	}
	
	public Comment commentEdit(int commentId, String commentText, int memberNo) {
		Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
		
		if(comment.getMemberNo() != memberNo) {
			throw new AccessDeniedException("본인 댓글만 수정할 수 있습니다.");
		}

		comment.setCommentText(commentText);
		commentRepo.save(comment);
		
		return comment;
	}
	
	@Transactional
	public void deleteComments(List<Integer> commentIds) {
		commentRepo.deleteAllById(commentIds);
	}
}
