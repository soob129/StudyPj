package com.study.project.DTO;

import java.time.LocalDateTime;

import com.study.project.model.PostStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
	private int postNo;
	private int memberNo;
	private String postCategory;
	private String postTitle;
	private String memberNickname;
	private int postView;
	private LocalDateTime postDate;
	private int commentCnt;
	private PostStatus postStatus;
}
