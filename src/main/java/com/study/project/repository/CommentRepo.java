package com.study.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.project.model.Comment;
import com.study.project.model.CommentStatus;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer>{
	
	public int countByMemberNo(int memberNo);
	
	public List<Comment> findByMemberNo(int memberNo);
	
	public Page<Comment> findByMemberNoAndCommentStatus(int memberNo, CommentStatus commentStatus, Pageable pageable);
	
	public int countByPostNo(int postNo);
	
	public List<Comment> findByPostNoAndCommentStatusOrderByCommentDateAsc(int postNo, CommentStatus commentStatus);
	
	public List<Comment> findByPostNoOrderByCommentDateAsc(int postNo);
}
