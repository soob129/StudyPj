package com.study.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.study.project.DTO.CommentDto;
import com.study.project.DTO.LikePostDto;
import com.study.project.DTO.MypageDto;
import com.study.project.DTO.PostDto;
import com.study.project.DTO.PostEditDto;
import com.study.project.DTO.UploadPostDto;
import com.study.project.model.Comment;
import com.study.project.model.Likeyo;
import com.study.project.model.Member;
import com.study.project.model.Post;
import com.study.project.security.MyUserDetails;
import com.study.project.service.CommentService;
import com.study.project.service.LikeyoService;
import com.study.project.service.MemberService;
import com.study.project.service.PostService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private LikeyoService likeyoService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/recruit")
	public String loadRecruit(){
		
		return "member/recruit";
	}
	
	@PostMapping("/recruitForm")
	public String signUp(@Valid @ModelAttribute UploadPostDto uploadPostDto, BindingResult bindingResult, @RequestParam(required=false) MultipartFile attachment,
			@AuthenticationPrincipal MyUserDetails userDetails, RedirectAttributes redirectAttributes) {
		
		if(bindingResult.hasErrors()) {
			String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			
			return "redirect:/member/recruit";
		}
		
		System.out.println(attachment);
		System.out.println(attachment == null ? "null" : attachment.isEmpty());
		
		Member loginMember = userDetails.getMember();
		
		postService.createPost(uploadPostDto, loginMember, attachment);
		redirectAttributes.addFlashAttribute("message", "게시글 등록이 완료되었습니다.");
		System.out.println("게시글 등록 성공: ");
		
		return "redirect:/user/showList";
	}
	
	@PostMapping("/deletePost/{id}")
	public String deletePost(@PathVariable int id, @AuthenticationPrincipal MyUserDetails userDetails) {
		
		Member loginMember = userDetails.getMember();
		
		postService.deletePost(id, loginMember);
		
		
		return "redirect:/user/showList";
	}
	
	@PostMapping("/editPost/{postNo}")
	@ResponseBody
	public PostEditDto editPost(@PathVariable int postNo, @RequestBody PostEditDto dto) {
		
		Post post = postService.editPost(postNo, dto);
		
		return new PostEditDto(post.getPostTitle(), post.getRecruitment(), post.getPosition(), post.getPostText());
	}
	
	@PostMapping("/likeToggle")
	@ResponseBody
	public String toggleLike(@RequestBody Map<String,Object> request, @AuthenticationPrincipal MyUserDetails user) {
		int postNo = Integer.parseInt(request.get("postNo").toString());
		int memberNo = user.getMember().getMemberNo();
		
		String status = likeyoService.likeToggle(postNo, memberNo);
		
		return status;
	}
	
	@GetMapping("/mypage")
	public String loadMypage(@AuthenticationPrincipal MyUserDetails user, Model model){
		
		int memberNo = user.getMember().getMemberNo();
		
		MypageDto mypage = memberService.loadMypage(memberNo);
		model.addAttribute("mypage", mypage);
		
		return "member/mypage";
	}
	
	@GetMapping("/posts")
	public String myPosts(@RequestParam(defaultValue= "0")int page, @AuthenticationPrincipal MyUserDetails user, Model model) {
		
		int memberNo = user.getMember().getMemberNo();
		
		Page<Post> postPage = postService.getMyPosts(memberNo, page);
		List<PostDto> posts = postService.convertListToDto(postPage.getContent());
		model.addAttribute("posts", posts);
		
		int[] pageGroup = postService.calculatePageGroup(postPage, 5);
		model.addAttribute("currentPage", pageGroup[0]);
		model.addAttribute("totPages", pageGroup[1]);
		model.addAttribute("currentGroupStart", pageGroup[2]);
		model.addAttribute("currentGroupEnd", pageGroup[3]);

		return "member/myPosts :: content";
	}
	
	@GetMapping("/comments")
	public String myComments(@RequestParam(defaultValue= "0")int page, @AuthenticationPrincipal MyUserDetails user, Model model) {
		
		int memberNo = user.getMember().getMemberNo();
		
		Page<Comment> commentList = commentService.getMyComments(memberNo, page);
		List<CommentDto> comments = commentService.commentToDto(commentList, memberNo);
		
		model.addAttribute("comments", comments);
		
		int[] pageGroup = postService.calculatePageGroup(commentList, 5);
		model.addAttribute("currentPage", pageGroup[0]);
		model.addAttribute("totPages", pageGroup[1]);
		model.addAttribute("currentGroupStart", pageGroup[2]);
		model.addAttribute("currentGroupEnd", pageGroup[3]);
	
		return "member/myComments :: content";
	}
	
	@GetMapping("/likes")
	public String myLikes(@RequestParam(defaultValue= "0")int page, @AuthenticationPrincipal MyUserDetails user, Model model) {
		
		int memberNo = user.getMember().getMemberNo();
		
		Page<Likeyo> likedPage = likeyoService.getMyLikes(memberNo, page);
		List<LikePostDto> likedPosts = likeyoService.likesToDto(likedPage, memberNo);
		
		model.addAttribute("likes", likedPosts);
		
		int[] pageGroup = postService.calculatePageGroup(likedPage, 5);
		model.addAttribute("currentPage", pageGroup[0]);
		model.addAttribute("totPages", pageGroup[1]);
		model.addAttribute("currentGroupStart", pageGroup[2]);
		model.addAttribute("currentGroupEnd", pageGroup[3]);
		
		//memberService.getMyLikes(memberNo, page, model);
	
		return "member/myLikes :: content";
	}
	
	@PostMapping("/submitComment")
	public String submitComment(int postNo, String commentText, @AuthenticationPrincipal MyUserDetails user, RedirectAttributes redirectAttributes) {
		
		if(commentText == null || commentText.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "댓글을 입력해주세요!");
			return "redirect:/user/postDetail?postNo=" + postNo;
		}

		int memberNo = user.getMember().getMemberNo();
		
		commentService.submitComment(memberNo, postNo, commentText);
		
		return "redirect:/user/postDetail?postNo=" + postNo;
	}
	
	@DeleteMapping("/commentDelete/{commentId}")
	@ResponseBody
	public ResponseEntity<String> commentDelete(@PathVariable int commentId, @AuthenticationPrincipal MyUserDetails user) {
		int memberNo = user.getMember().getMemberNo();
		
		commentService.commentDelete(commentId, memberNo);
		
		return ResponseEntity.ok("success");
	}
	
	@PostMapping("/editComment/{commentId}")
	@ResponseBody
	public Comment commentEdit(@PathVariable int commentId, @RequestBody Map<String, String> data, @AuthenticationPrincipal MyUserDetails user) {
		String commentText = data.get("commentText");
		int memberNo = user.getMember().getMemberNo();
		
		Comment comment = commentService.commentEdit(commentId, commentText, memberNo);
		
		return comment;
	}
	
	@PostMapping("/deletePosts")
	@ResponseBody
	public Map<String, Object> deletePosts(@RequestBody List<Integer> postNos) {
		
		postService.deletePosts(postNos);
		
		return Map.of("success", true);
	}
	
	@PostMapping("/deleteComments")
	@ResponseBody
	public Map<String, Object> deleteComments(@RequestBody List<Integer> commentIds) {
		
		commentService.deleteComments(commentIds);
		
		return Map.of("success", true);
	}
	
	@PostMapping("/cancelLikes")
	@ResponseBody
	public Map<String, Object> cancelLikes(@RequestBody List<Long> likeyoNos){
		
		likeyoService.cancelLikes(likeyoNos);
		
		return Map.of("success", true);
	}

}
