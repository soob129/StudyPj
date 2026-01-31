package com.study.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="POST")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
	@SequenceGenerator(name = "post_seq", sequenceName = "POST_NO_SEQ", allocationSize = 1)
	@Column(name = "POST_NO")
	private int postNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_NO")
	private Member member;
	
	@Column(name = "POST_CATEGORY")
	private String postCategory;
	
	@Column(name = "POST_TITLE", length = 50, nullable = false)
	private String postTitle;
	
	@Column(name = "POST_DUE")
	private LocalDate postDue;
	
	@Column(name = "POST_DATE")
	private LocalDateTime postDate;
	
	@Lob
	@Column(name = "POST_TEXT")
	private String postText;
	
	@Column(name = "POST_VIEW")
	private int postView;
	
	@Column(name = "POST_PROCEED", length = 20, nullable = false)
	private String postProceed;
	
	@Column(name = "POST_PERIOD", length= 20, nullable = false)
	private String postPeriod;
	
	@Column(name = "RECRUITMENT", length = 20, nullable = false)
	private String recruitment;
	
	@Column(name = "POST_POSITION", length = 100, nullable = false)
	private String position;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "POST_STATUS", length = 30, nullable = false)
	private PostStatus postStatus = PostStatus.ACTIVE;
	
}
