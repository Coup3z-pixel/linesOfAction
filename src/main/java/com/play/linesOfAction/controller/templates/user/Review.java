package com.play.linesOfAction.controller.templates.user;

import java.net.http.HttpHeaders;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.apache.http.client.methods.HttpHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
		model.addAttribute("content", "review/review");

		if (idOfGames == null) {
			return "layout";
		}
		
		List<Game> games = playerTemplate.getGames(idOfGames);	
		
		model.addAttribute("gameList", games);
		
		return "layout";
	}

	@GetMapping("/user/review/game/{gameId}")
	public String gameReviewPage(@PathVariable String gameId, Model model, Principal principal, Authentication authentication) {
		// Check if the gameId is part of user's history
	
		if (!playerTemplate.isGameInUserHistory("5019530a-0f1d-43f1-b10b-c5463f97c688", gameId))
			return "redirect:" + "/play";
			
		Optional<Game> possibleGame = gameRepository.findById(gameId);

		if (!possibleGame.isPresent())
			return "redirect:" + "/play";
		
		model.addAttribute("content", "review/review");

		Game userGame = possibleGame.get();


		// Create the front end interaction
		// pass usergame through
		
		return "layout";
	}
}
