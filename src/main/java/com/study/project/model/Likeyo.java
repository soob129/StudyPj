package com.study.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="LIKEYO")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Likeyo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "likeyo_seq")
	@SequenceGenerator(name = "likeyo_seq", sequenceName = "LIKEYO_NO_SEQ", allocationSize = 1)
	@Column(name = "LIKEYO_NO")
	private long likeyoNo;
	
	@Column(name = "POST_NO")
	private int postNo;
	
	@Column(name = "MEMBER_NO")
	private int memberNo;
	
	@Column(name = "STATUS", length=2, nullable=false)
	private String status;
	
	
}
