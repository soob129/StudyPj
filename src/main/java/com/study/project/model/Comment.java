package com.study.project.model;

import java.time.LocalDateTime;

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
@Table(name="POST_COMMENT")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
	@SequenceGenerator(name = "comment_seq", sequenceName = "COMMENT_NO_SEQ", allocationSize = 1)
	@Column(name = "COMMENT_ID")
	private int commentId;
	
	@Column(name = "POST_NO")
	private int postNo;
	
	@Column(name = "MEMBER_NO")
	private int memberNo;
	
	@Column(name = "COMMENT_TEXT", length = 500, nullable = false)
	private String commentText;
	
	@Column(name = "COMMENT_DATE")
	private LocalDateTime commentDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "COMMENT_STATUS", length = 30, nullable = false)
	private CommentStatus commentStatus = CommentStatus.ACTIVE;
}
