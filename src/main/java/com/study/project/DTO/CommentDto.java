package com.study.project.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	private int commentId;
	private int postNo;
	private String commentText;
	private int memberNo;
	private LocalDateTime commentDate;
	private String postTitle;
}
