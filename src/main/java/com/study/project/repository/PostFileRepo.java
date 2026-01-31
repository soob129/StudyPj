package com.study.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.project.model.PostFile;

@Repository
public interface PostFileRepo extends JpaRepository<PostFile, String>{
	public PostFile findByPostNo(int postNo);
}
