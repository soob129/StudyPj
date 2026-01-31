package com.study.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.study.project.DTO.MemberDto;
import com.study.project.DTO.MemberInfoDto;
import com.study.project.DTO.PostDetailDto;
import com.study.project.DTO.PostDto;
import com.study.project.model.Post;
import com.study.project.repository.MemberRepo;
import com.study.project.security.MyUserDetails;
import com.study.project.service.MemberService;
import com.study.project.service.PostService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private MemberRepo memberRepo;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PostService postService;
	
	@GetMapping("/login")
	public String loadLogin(){
		
		return "user/login";
	}
	
	@GetMapping("/signUp")
	public String loadSignUp() {
		
		return "user/signUp";
	}
	
	@PostMapping("/signupForm")
	public String signUp(@ModelAttribute MemberDto memberDto, Model model, RedirectAttributes redirectAttributes) {
		try {
			memberService.registerMember(memberDto);
			redirectAttributes.addFlashAttribute("message", "회원가입이 성공적으로 완료되었습니다!");
			System.out.println("회원가입 성공! 회원 아이디: " + memberDto.getUserId());
			
			return "redirect:/user/login";
		}catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/user/signUp"; 
		}catch (DataIntegrityViolationException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "필수 항목을 모두 입력해주세요");
			return "redirect:/user/signUp"; 
		}catch (RuntimeException e){
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			System.out.println("회원가입 실패" + e.getMessage());
			return "redirect:/user/signUp"; 
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
			System.out.println("회원가입 실패");
	        return "redirect:/user/signUp"; 
		}
		
	}
	
	@GetMapping("/checkNickname")
	@ResponseBody
	public String checkNickname(@RequestParam String nickname) {
		boolean exists = memberRepo.existsByMemberNickname(nickname);
		return exists ? "DUPLICATE" : "OK";
	}
	
	@GetMapping("/findId")
	public String loadFindId() {
		
		return "user/findId";
	}
	
	@PostMapping("/findidForm")
	public String findId(@RequestParam String userName, @RequestParam String userEmail, Model model) {
		MemberInfoDto info = memberService.findMemberId(userName, userEmail);
		
		if(info == null) {
			model.addAttribute("errorMessage", "일치하는 회원이 없습니다.");
			return "user/findId";
		}

		model.addAttribute("memberId", info.getMemberId());
		model.addAttribute("joinDate", info.getJoinDate());
		
		return "user/findIdRst";
	}
	
	@GetMapping("/findPw")
	public String loadFindPw() {
		
		return "user/findPw";
	}
	
	@PostMapping("/findpwForm")
	public String findPw(@RequestParam String userId,@RequestParam String userName, @RequestParam String userEmail, Model model) {
		
		String memberId = memberService.findMemberPw(userId, userName, userEmail);
		
		if(memberId != null) {
			model.addAttribute("userId", memberId);
			return "user/resetPw";
		} else {
			model.addAttribute("errorMessage", "회원정보를 찾을 수 없습니다.");
			return "user/findPw";
		}
	}
	
	@PostMapping("/resetPw")
	public String resetPw(@RequestParam String memberId, @RequestParam String userPw, @RequestParam String pwCheck, Model model) {
		
		System.out.println("********************");
		System.out.println(memberId);
		
		if(!userPw.equals(pwCheck)) {
			model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("userId", memberId);
			return "user/resetPw";
		} 
		
		boolean isPastPw = memberService.pwCheck(memberId, userPw);
		if(isPastPw) {
			model.addAttribute("errorMessage", "기존의 비밀번호와 다르게 입력해주세요.");
			model.addAttribute("userId", memberId);
			return "user/resetPw";
		}
		
		memberService.resetPw(memberId, userPw);
		model.addAttribute("errorMessage", "비밀번호 변경 성공!");
		return "user/login";

	}
	
	@GetMapping("/showList")
	public String showList(@RequestParam(required=false) String proceed, @PageableDefault(size=10, sort="postDate", direction=Sort.Direction.DESC)Pageable pageable, Model model) {
		
		//userService.showList(pageable, model);
		
		List<PostDto> bestPosts;
		Page<Post> list;
		
		if(proceed != null && !proceed.isEmpty()) {
			bestPosts = postService.showBestPostsByProceed(proceed);
			list = postService.showListByProceed(proceed, pageable);
		} else {
			bestPosts = postService.popularPosts();
			list = postService.showList(pageable);
		}
		
		int[] pageGroup = postService.calculatePageGroup(list, 5);
		
		model.addAttribute("bestPosts", bestPosts);
		model.addAttribute("list", list.getContent());
		model.addAttribute("currentPage", pageGroup[0]);
		model.addAttribute("totPages", pageGroup[1]);
		model.addAttribute("currentGroupStart", pageGroup[2]);
		model.addAttribute("currentGroupEnd", pageGroup[3]);
		
		return "user/list";
	}
	
	@GetMapping("/showList/fragment")
	public String showListFragment(@RequestParam(defaultValue= "0") int page, @RequestParam(required=false) String search, Model model) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("postDate").descending());
		
		//userService.showListBySearch(search, pageable, model);
		
		Page<Post> list = postService.searchPosts(search, pageable);
		
		int[] pageGroup = postService.calculatePageGroup(list, 5);
		
		model.addAttribute("list", list.getContent());
		model.addAttribute("search", search);
		model.addAttribute("currentPage", pageGroup[0]);
		model.addAttribute("totPages", pageGroup[1]);
		model.addAttribute("currentGroupStart", pageGroup[2]);
		model.addAttribute("currentGroupEnd", pageGroup[3]);
		
		
		return "user/list::listFragment";
	}
	
	
	@GetMapping("/postDetail")
	public String postDetail(@RequestParam int postNo, Model model, @AuthenticationPrincipal MyUserDetails user) {
		
		int memberNo = (user != null) ? user.getMember().getMemberNo() : -1;
		
		PostDetailDto dto = postService.getPostDetail(memberNo, postNo);
		
		System.out.println(dto.getFile().getFilePath());
		
		model.addAttribute("post", dto.getPost());
		model.addAttribute("file", dto.getFile());
		model.addAttribute("comments", dto.getComments());
		model.addAttribute("isBookmarked", dto.isBookmarked());
		
		return "user/detail";
	}

}
