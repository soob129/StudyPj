package com.study.project.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="POST_FILE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostFile {
	 @Id
	 @Column(name = "FILE_NO", nullable = false, unique = true)
	 private String fileNo;
	 
	 @Column(name = "POST_NO")
	 private int postNo;
	 
	 @Column(name = "FILE_PATH", length = 255, nullable = false)
	 private String filePath;
	 
	 @Column(name = "FILE_NAME", length = 255, nullable = false)
	 private String fileName;
	 
	 @Column(name = "FILE_DATE", nullable = false)
	 @Temporal(TemporalType.DATE)
	 private Date fileDate;
	 
}
