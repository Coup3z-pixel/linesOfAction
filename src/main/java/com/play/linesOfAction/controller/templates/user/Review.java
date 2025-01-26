package com.play.linesOfAction.controller.templates.user;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.play.linesOfAction.controller.db.GameRepository;
import com.play.linesOfAction.controller.db.PlayerTemplate;
import com.play.linesOfAction.model.game.Game;

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
		List<String> idOfGames = playerTemplate.getIdOfGames("5019530a-0f1d-43f1-b10b-c5463f97c688");
		System.out.println("Id Of Games: " + idOfGames);

		// Returning page to keep games in scope
		model.addAttribute("content", "review/review :: gameReview");

		if (idOfGames == null) {
			return "layout";
		}
		
		List<Game> games = playerTemplate.getGames(idOfGames);	
		
		model.addAttribute("gameList", games);
		
		return "layout";
	}
}
