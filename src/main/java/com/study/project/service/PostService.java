package com.study.project.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.study.project.DTO.PostCommentDto;
import com.study.project.DTO.PostDetailDto;
import com.study.project.DTO.PostDto;
import com.study.project.DTO.PostEditDto;
import com.study.project.DTO.UploadPostDto;
import com.study.project.model.Comment;
import com.study.project.model.CommentStatus;
import com.study.project.model.Member;
import com.study.project.model.Post;
import com.study.project.model.PostFile;
import com.study.project.model.PostStatus;
import com.study.project.repository.CommentRepo;
import com.study.project.repository.LikeyoRepo;
import com.study.project.repository.MemberRepo;
import com.study.project.repository.PostFileRepo;
import com.study.project.repository.PostRepo;

@Service
public class PostService {
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private LikeyoRepo likeyoRepo;
	
	@Autowired
	private MemberRepo memberRepo;
	
	@Autowired
	private PostFileRepo postFileRepo;
	
	public List<PostDto> popularPosts(){
		LocalDate today = LocalDate.now();
		List<Post> posts = postRepo.findTop4ByPostDueGreaterThanEqualAndPostStatusOrderByPostViewDesc(today, PostStatus.ACTIVE);
		List<PostDto> popularPosts = convertListToDto(posts);
		
		return popularPosts;
	}
	
	public Page<Post> showList(Pageable pageable){
		Page<Post> list = postRepo.findAllByPostStatus(PostStatus.ACTIVE, pageable);
		
		return list;
	}
	
	public Page<Post> showListByProceed(String proceed, Pageable pageable){
		Page<Post> list = postRepo.findAllByPostProceedAndPostStatus(proceed, PostStatus.ACTIVE ,pageable);
		
		return list;
	}
	
	public List<PostDto> showBestPostsByProceed(String proceed){
		LocalDate today = LocalDate.now();
		List<Post> posts = postRepo.findTop4ByPostDueGreaterThanEqualAndPostProceedAndPostStatusOrderByPostViewDesc(today, PostStatus.ACTIVE, proceed);
		List<PostDto> popularPosts = convertListToDto(posts);
		
		return popularPosts;
	}
	
	public Page<Post> searchPosts(String search, Pageable pageable){
		if(search != null && !search.trim().isEmpty()) {
			return postRepo.findByPostTitleContainingIgnoreCaseAndPostStatus(search, PostStatus.ACTIVE, pageable);
		} else {
			return postRepo.findAll(pageable);
		}
	}
	
	public int[] calculatePageGroup(Page<?> page, int size) {
		int currentPage = page.getNumber();
		int totPages = page.getTotalPages();
		int currentGroupStart = (currentPage / size) * size;
		int currentGroupEnd = Math.min(currentGroupStart + size - 1, totPages - 1);
		return new int[] {currentPage, totPages, currentGroupStart, currentGroupEnd};
	}
	
	public PostDto postToDto(Post post) {
		PostDto tmp = new PostDto();
		tmp.setPostNo(post.getPostNo());
		tmp.setMemberNo(post.getMember().getMemberNo());
		tmp.setPostCategory(post.getPostCategory());
		tmp.setPostTitle(post.getPostTitle());
		tmp.setPostView(post.getPostView());
		tmp.setPostDue(post.getPostDue());
		tmp.setPostDate(post.getPostDate());
		
		int commentCnt = commentRepo.countByPostNo(post.getPostNo());
		tmp.setCommentCnt(commentCnt);
		
		return tmp;
	}
	
	public List<PostDto> convertListToDto(List<Post> posts) {
		return posts.stream()
				.map(this::postToDto).collect(Collectors.toList());
	}
	
	@Transactional
	public PostDetailDto getPostDetail(int memberNo, int postNo) {
		Post post = postRepo.findByPostNo(postNo);
		
		boolean isBookmarked = (memberNo != -1) && likeyoRepo.existsByMemberNoAndPostNo(memberNo, postNo);
		
		List<Comment> commentList = commentRepo.findByPostNoAndCommentStatusOrderByCommentDateAsc(postNo, CommentStatus.ACTIVE);
		
		PostFile file = postFileRepo.findByPostNo(postNo);
		
		List<PostCommentDto> comments = commentList.stream().map(c -> {
			PostCommentDto dto = new PostCommentDto();
			int cMemberNo = c.getMemberNo();
			
			dto.setCommentId(c.getCommentId());
			dto.setPostNo(c.getPostNo());
			dto.setCommentText(c.getCommentText());
			dto.setMemberNo(cMemberNo);
			dto.setCommentDate(c.getCommentDate());
			dto.setMemberNickname(memberRepo.findByMemberNo(cMemberNo).getMemberNickname());
			dto.setCommentStatus(c.getCommentStatus());
			
			return dto;
		}).collect(Collectors.toList());
		
		post.setPostView(post.getPostView() + 1);
		
		return new PostDetailDto(post, file, comments, isBookmarked);
	}
	
	public void createPost(UploadPostDto dto, Member member, MultipartFile attachment) {
		Post post = new Post();
		post.setMember(member);
		post.setPostCategory(dto.getCategory());
		post.setPostTitle(dto.getTitle());
		post.setPostDue(dto.getDueDate());
		post.setPostDate(LocalDateTime.now());
		post.setPostText(dto.getContent());
		post.setPostProceed(dto.getProceed());
		post.setPostPeriod(dto.getPeriod());
		post.setRecruitment(dto.getRecruitment());
		
	
		String rst = String.join(",", dto.getPosition());
		post.setPosition(rst);
		
		Post savePost = postRepo.save(post);
		
		if (attachment != null && !attachment.isEmpty()) {
			String uuid = UUID.randomUUID().toString();	
			String originName = attachment.getOriginalFilename();		
			String newName = uuid + "_" + originName;
					
			String uploadDir = System.getProperty("user.dir") + "/uploads/";
					
			try {
				Path filePath = Paths.get(uploadDir, newName);
				Files.createDirectories(filePath.getParent());
				attachment.transferTo(filePath.toFile());
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("파일 업로드 실패!");
			}
					
					
			PostFile postFile = new PostFile();
			postFile.setFileNo(uuid);
			postFile.setPostNo(savePost.getPostNo());
			postFile.setFilePath("/uploads/" + newName);
			postFile.setFileName(originName);
			postFile.setFileDate(new Date());
					
			postFileRepo.save(postFile);
					
			
		}
			
		
	}
	
	public void deletePost(int postNo, Member member) {
		Post post = postRepo.findByPostNo(postNo);
		
		if (post.getMember().getMemberNo() != member.getMemberNo()) {
			throw new AccessDeniedException("삭제 권한이 없습니다.");
		}
		
		post.setPostStatus(PostStatus.DELETED);
	}
	
	public Post editPost(int postNo, PostEditDto dto) {
		Post post = postRepo.findByPostNo(postNo);
		
		post.setPostTitle(dto.getPostTitle());
		post.setRecruitment(dto.getRecruitment());
		post.setPosition(dto.getPosition());
		post.setPostText(dto.getPostText());
		
		postRepo.save(post);
		
		return post;
	}
	
	public Page<Post> getMyPosts(int memberNo, int page) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("postDate").descending());
		Page<Post> postPage = postRepo.findByMember_MemberNo(memberNo, pageable);
		
		return postPage;
	}
	
	@Transactional
	public void deletePosts(List<Integer> postNos) {
		postRepo.deleteAllById(postNos);
	}
}
