package com.study.project.DTO;

import java.util.List;

import com.study.project.model.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailAdminDto {
	private Post post; 
	private List<PostCommentDto> comments;
}
