package com.study.project.DTO;

import java.time.LocalDate;

import com.study.project.model.MemberStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailAdminDto {
	private int memberNo;
	private String memberId;
	private String memberNickname;
	private LocalDate joinDate;
	private MemberStatus memberStatus;
	private String memberRole;
	private String memberEmail;
	private String memberPhone;
	private int postCnt;
	private int commentCnt;
}
