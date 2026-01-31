package com.study.project.DTO;

import java.time.LocalDate;

import com.study.project.model.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MemberInfoDto {
	private String memberNickname;
	private String memberId;
	private String memberName;
	private LocalDate memberBirth;
	private String maskedEmail;
	private String memberGender;
	private String maskedPhone;
	private LocalDate joinDate;
	
	public MemberInfoDto(Member member) {
		this.memberNickname = member.getMemberNickname();
		this.memberId = member.getMemberId();
		this.memberName = member.getMemberName();
		this.memberBirth = member.getMemberBirth();
		this.maskedEmail = maskEmail(member.getMemberEmail()); 
		this.memberGender = member.getMemberGender();
		this.maskedPhone = maskPhone(member.getMemberPhone());
		this.joinDate = member.getJoinDate();
	}
	
	private String maskEmail(String email) {
		String[] parts = email.split("@");
		String id = parts[0];
		String domain = parts[1];
		
		String maskedId = "*".repeat(id.length());
		
		return maskedId + "@" + domain;
	}
	
	private String maskPhone(String phone) {
		if(phone.length() == 11) { return "010-****-" + phone.substring(phone.length()-4);}
		return phone;
	}
}
