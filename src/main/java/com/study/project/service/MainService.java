package com.study.project.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.study.project.model.Post;
import com.study.project.model.PostStatus;
import com.study.project.repository.PostRepo;

@Service
public class MainService {
	
	@Autowired
	private PostRepo postRepo;
	
	public void goMainSite(Model model) {
		// 조회수 높은 스터디 구하기
		LocalDate today = LocalDate.now();
		List<Post> postList = postRepo.findTop4ByPostDueGreaterThanEqualAndPostStatusOrderByPostViewDesc(today, PostStatus.ACTIVE);
		
		
		
		model.addAttribute("list", postList);
	}

}
