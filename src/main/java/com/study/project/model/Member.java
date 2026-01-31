package com.study.project.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="MEMBER")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
	@SequenceGenerator(name = "member_seq", sequenceName = "MEMBER_NO_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_NO")
	private int memberNo;
	
	@Column(name = "MEMBER_ID", length = 50, nullable = false)
	private String memberId;
	
	@Column(name = "MEMBER_PW", length = 150, nullable = false)
	private String memberPw;
	
	@Column(name = "MEMBER_NAME", length = 30, nullable = false)
	private String memberName;
	
	@Column(name = "MEMBER_PHONE", length = 30, nullable = false)
	private String memberPhone;

	@Column(name = "MEMBER_BIRTH")
	private LocalDate memberBirth;
	
	@Column(name = "MEMBER_EMAIL", length = 50, nullable = false)
	private String memberEmail;
	
	@Column(name = "MEMBER_GENDER", length = 1)
	private String memberGender;
	
	@Column(name = "MEMBER_ROLE", length = 30, nullable = false)
	private String memberRole;
	
	@Column(name = "MEMBER_NICKNAME", length = 30, nullable = false)
	private String memberNickname;
	
	@Column(name = "JOIN_DATE")
	private LocalDate joinDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "MEMBER_STATUS", length = 30, nullable = false)
	private MemberStatus memberStatus = MemberStatus.ACTIVE;
}
