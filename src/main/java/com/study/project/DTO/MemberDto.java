package com.study.project.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
	private String userId;
	private String userPw;
	private String userNickname;
	private String userName;
	private LocalDate userBirth;
	private String userGender;
	private String userPhone;
	private String userEmail;
	
	public void validate() {
		if(userId == null || userId.isEmpty()) throw new IllegalArgumentException("아이디를 입력해주세요.");
		
		if(userPw == null || userPw.isEmpty()) throw new IllegalArgumentException("비밀번호를 입력해주세요.");
		
		if(userNickname == null || userNickname.isEmpty()) throw new IllegalArgumentException("닉네임을 입력해주세요.");
		
		if(userName == null || userName.isEmpty()) throw new IllegalArgumentException("이름을 입력해주세요.");
		
		if(userBirth == null) throw new IllegalArgumentException("생년월일을 입력해주세요.");
		
		if(userGender == null || userGender.isEmpty()) throw new IllegalArgumentException("성별을 입력해주세요.");
		
		if(userPhone == null || userPhone.isEmpty()) throw new IllegalArgumentException("전화번호를 입력해주세요.");
		
		if(userEmail == null || userEmail.isEmpty()) throw new IllegalArgumentException("이메일을 입력해주세요.");
	}
}
