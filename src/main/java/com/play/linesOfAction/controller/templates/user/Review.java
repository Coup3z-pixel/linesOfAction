package com.play.linesOfAction.controller.templates.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Review
 */
@Controller
public class Review {

	@GetMapping("/user/review")
	public String reviewPage(Model model) {
		model.addAttribute("content", "review/review");
		return "layout";
	}
}
