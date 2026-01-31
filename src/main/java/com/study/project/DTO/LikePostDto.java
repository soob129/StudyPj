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
public class LikePostDto {
	private long likeyoNo;
	private int postNo;
	private int memberNo;
	private String memberNickname;
	private String postTitle;
	private int postView;
	private LocalDateTime postDate;
	private int commentCnt;
}
