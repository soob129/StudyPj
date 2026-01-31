package com.study.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.study.project.model.Post;
import com.study.project.model.PostStatus;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {
	
	public List<Post> findTop4ByPostDueGreaterThanEqualAndPostStatusOrderByPostViewDesc(LocalDate today, PostStatus postStatus);
	
	public Page<Post> findAllByPostStatus(PostStatus postStatus, Pageable pageable);
	
	public Post findByPostNo(int postNo);
	
	public List<Post> findTop4ByPostDueGreaterThanEqualAndPostProceedAndPostStatusOrderByPostViewDesc(LocalDate today, PostStatus postStatus, String proceed);
	
	public Page<Post> findAllByPostProceedAndPostStatus(String proceed, PostStatus postStatus, Pageable pageable);
	
	public int countByMember_MemberNo(int memberNo);
	
	public List<Post> findByMember_MemberNo(int memberNo);
	
	public Page<Post> findByMember_MemberNo(int memberNo, Pageable pageable);
	
	public Page<Post> findByPostTitleContainingIgnoreCaseAndPostStatus(String title, PostStatus postStatus, Pageable pageable);
	
	@Query("SELECT MONTH(p.postDate) as month, COUNT(p) as count " + "FROM Post p " + "GROUP BY MONTH(p.postDate)")
	List<Object[]> countPostsByMonth();
	
}
