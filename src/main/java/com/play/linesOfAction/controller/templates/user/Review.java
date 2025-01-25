package com.play.linesOfAction.controller.templates.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.play.linesOfAction.controller.db.GameRepository;
import com.play.linesOfAction.controller.db.PlayerTemplate;

/**
 * Review
 */
@Controller
public class Review {

	@Autowired
	GameRepository gameRepository;

	@Autowired
	PlayerTemplate playerTemplate;

	@GetMapping("/user/review")
	public String reviewPage(Model model, Principal principal, Authentication authentication) {
		Object games = playerTemplate.getGames("5019530a-0f1d-43f1-b10b-c5463f97c688");
		System.out.println(games);
		
		model.addAttribute("content", "review/review");
		return "layout";
	}
}
