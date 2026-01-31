package com.study.project.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.project.DTO.MemberDetailAdminDto;
import com.study.project.DTO.MemberListAdminDto;
import com.study.project.DTO.PostDetailAdminDto;
import com.study.project.DTO.PostListDto;
import com.study.project.service.AdminService;
import com.study.project.service.PostService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private PostService postService;
	
	@GetMapping("/main")
	public String loadAdmin(){
		
		return "admin/admin";
	}
	
	@GetMapping("/dashboard")
	public String loadDashboard(Model model) throws JsonProcessingException {
		long memberCnt = adminService.getMemberCnt();
		long postCnt = adminService.getPostCnt();
		long commentCnt = adminService.getCommentCnt();
		
		model.addAttribute("memberCnt", memberCnt);
		model.addAttribute("postCnt", postCnt);
		model.addAttribute("commentCnt", commentCnt);
		
		Map<Integer, Long> monthlyPosts = adminService.getMonthlyPosts();
		
		ObjectMapper mapper = new ObjectMapper();
		String monthlyPostsJson = mapper.writeValueAsString(monthlyPosts);
		
		model.addAttribute("monthlyPosts", monthlyPostsJson);
		
		System.out.print(monthlyPosts);
		
		return "admin/dashboard :: content";
	}
	
	@GetMapping("/users")
	public String loadMembers(@RequestParam(defaultValue= "0")int page, Model model) {
		int size = 10;
		
		Page<MemberListAdminDto> members = adminService.getMembersPage(page, size);
		model.addAttribute("members", members);
		
		int[] pageGroup = postService.calculatePageGroup(members, 5);
		model.addAttribute("currentPage", pageGroup[0]);
		model.addAttribute("totPages", pageGroup[1]);
		model.addAttribute("currentGroupStart", pageGroup[2]);
		model.addAttribute("currentGroupEnd", pageGroup[3]);
		
		return "admin/users :: content";
	}
	
	@GetMapping("/posts")
	public String loadPosts(@RequestParam(defaultValue= "0")int page, Model model) {
		int size = 10;
		
		Page<PostListDto> posts = adminService.getPostsPage(page, size);
		model.addAttribute("posts", posts);
		
		int[] pageGroup = postService.calculatePageGroup(posts, 5);
		model.addAttribute("currentPage", pageGroup[0]);
		model.addAttribute("totPages", pageGroup[1]);
		model.addAttribute("currentGroupStart", pageGroup[2]);
		model.addAttribute("currentGroupEnd", pageGroup[3]);
		
		return "admin/posts :: content";
	}
	
	@GetMapping("/userDetail")
	public String getUserDetail(@RequestParam int memberNo, Model model) {
		MemberDetailAdminDto dto = adminService.getMemberDetail(memberNo);
		
		model.addAttribute("member", dto);
		
		return "admin/userDetail :: content";
	}
	
	@PostMapping("/deleteMember")
	@ResponseBody
	public Map<String, Boolean> deleteMember(@RequestBody Map<String, Integer> request) {
		int memberNo = request.get("memberNo");
		
		boolean success = adminService.deleteMember(memberNo);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("success", success);
		
		return result;
	}
	
	@PostMapping("/restoreMember")
	@ResponseBody
	public Map<String, Boolean> restoreMember(@RequestBody Map<String, Integer> request) {
		int memberNo = request.get("memberNo");
		
		boolean success = adminService.restoreMember(memberNo);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("success", success);
		
		return result;
	}
	
	@GetMapping("/postDetail")
	public String getPostDetail(@RequestParam int postNo, Model model) {
		PostDetailAdminDto dto = adminService.getPostDetail(postNo);
		
		int memberNo = dto.getPost().getMember().getMemberNo();
		
		MemberDetailAdminDto member = adminService.getMemberDetail(memberNo);
		
		model.addAttribute("post", dto.getPost());
		model.addAttribute("comments", dto.getComments());
		model.addAttribute("member", member);
		
		return "admin/postDetail :: content";
	}
	
	@PostMapping("/deletePost")
	@ResponseBody
	public Map<String, Boolean> deletePost(@RequestBody Map<String, Integer> request){
		int postNo = request.get("postNo");
		
		boolean success = adminService.deletePost(postNo);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("success", success);
		
		return result;	
	}
	
	@PostMapping("/restorePost")
	@ResponseBody
	public Map<String, Boolean> restorePost(@RequestBody Map<String, Integer> request){
		int postNo = request.get("postNo");
		
		boolean success = adminService.restorePost(postNo);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("success", success);
		
		return result;	
	}
}
