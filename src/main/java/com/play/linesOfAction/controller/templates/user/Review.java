package com.play.linesOfAction.controller.templates.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.play.linesOfAction.controller.db.GameRepository;

/**
 * Review
 */
@Controller
public class Review {

	@Autowired
	GameRepository gameRepository;

	@GetMapping("/user/review")
	public String reviewPage(Model model) {
		model.addAttribute("content", "review/review");
		return "layout";
	}
}
