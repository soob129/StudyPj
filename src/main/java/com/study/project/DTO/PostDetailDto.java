package com.study.project.DTO;

import java.util.List;

import com.study.project.model.Post;
import com.study.project.model.PostFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {
	private Post post; 
	private PostFile file;
	private List<PostCommentDto> comments;
	private boolean isBookmarked;
}
