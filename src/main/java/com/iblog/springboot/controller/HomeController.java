package com.iblog.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.iblog.springboot.entity.Post;
import com.iblog.springboot.service.PostService;

@Controller
public class HomeController {

	@Autowired
	private PostService postService;

	// ================= HOME =================

	@GetMapping("/")
	public String home(Model model) {

		// Show only blogs approved by admin
		List<Post> posts = postService.getApprovedPosts();

		model.addAttribute("posts", posts);

		return "iblog";
	}

	// ================= ABOUT =================

	@GetMapping("/about")
	public String about() {

		return "iblogabout";
	}

}