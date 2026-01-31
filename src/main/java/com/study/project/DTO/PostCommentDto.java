package com.study.project.DTO;

import java.time.LocalDateTime;

import com.study.project.model.CommentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDto {
	private int commentId;
	private int postNo;
	private String commentText;
	private int memberNo;	// 댓글 쓴 사람
	private LocalDateTime commentDate;
	private String memberNickname;
	private CommentStatus commentStatus;
}
