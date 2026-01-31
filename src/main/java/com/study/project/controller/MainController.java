package com.study.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.project.service.MainService;

@Controller
@RequestMapping("/show")
public class MainController {

	@Autowired
	private MainService mainService;
	
	@GetMapping("/Main")
	public String goMainSite(Model model) {
		
		mainService.goMainSite(model);
		
		return "main/main";
	}
}
