package com.study.project.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
	private int postNo;
	private int memberNo;
	private String postCategory;
	private String postTitle;
	private int postView;
	private LocalDate postDue;
	private LocalDateTime postDate;
	private int commentCnt;
}
