package com.study.project.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadPostDto {
	private String category; 
	
	@NotBlank(message="제목을 입력해주세요")
	private String title;
	
	@NotBlank(message="진행방식을 입력해주세요")
	private String proceed;
	
	@NotBlank(message="진행기간을 입력해주세요")
	private String period;
	
	@NotEmpty(message = "포지션을 입력해주세요")
	private List<String> position = new ArrayList<>();
	
	@NotBlank(message="모집인원을 입력해주세요")
	private String recruitment;
	
	@NotNull(message="마감일을 선택해주세요")
	private LocalDate dueDate;
	
	@NotNull(message="내용을 입력해주세요")
	private String content;
}
