package com.study.project.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MypageDto {
	private String nickname;
	private int postCnt;
	private int commentCnt;
	private int likeCnt;
}
