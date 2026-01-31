package com.study.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostEditDto {
	private String postTitle;
	private String recruitment;
	private String position;
	private String postText;
}
