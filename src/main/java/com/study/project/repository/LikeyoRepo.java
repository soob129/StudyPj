package com.study.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.project.model.Likeyo;

@Repository
public interface LikeyoRepo extends JpaRepository<Likeyo, Long>{
	public Optional<Likeyo> findByPostNoAndMemberNo(int postNo, int memberNo);
	
	public int countByMemberNo(int memberNo);
	
	public List<Likeyo> findByMemberNo(int memberNo); 
	
	public Boolean existsByMemberNoAndPostNo(int postNo, int memberNo);
	
	public Page<Likeyo> findByMemberNo(int memberNo, Pageable pageable);
}
