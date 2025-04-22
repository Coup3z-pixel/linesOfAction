package com.play.linesOfAction.controller.templates.user;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.play.linesOfAction.controller.db.GameRepository;
import com.play.linesOfAction.controller.db.PlayerTemplate;
import com.play.linesOfAction.model.game.Game;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
	public String reviewPage(
			Model model, 
			HttpServletRequest request, 
			HttpServletResponse response
		) {

		Optional<String> optionalUserId = this.getUserIdCookie(request);

		if (!optionalUserId.isPresent())
			return "redirect:/sign";

		String userId = optionalUserId.get();
		
		List<String> idOfGames = playerTemplate.getIdOfGames(userId);

		model.addAttribute("content", "review/review");

		if (idOfGames == null) {
			return "layout";
		}
		
		List<Game> games = playerTemplate.getGames(idOfGames);	
		
		model.addAttribute("gameList", games);
		
		return "layout";
	}

	@GetMapping("/user/review/game/{gameId}")
	public String gameReviewPage(
			@PathVariable String gameId, 
			Model model, 
			HttpServletRequest request, 
			HttpServletResponse response
		) {

		Optional<String> optionalUserId = this.getUserIdCookie(request);

		if (!optionalUserId.isPresent())
			return "redirect:/sign";

		String userId = optionalUserId.get();

		// Check if the gameId is part of user's history	
		if (!playerTemplate.isGameInUserHistory(userId, gameId))
			return "redirect:/play";
			
		Optional<Game> possibleGame = gameRepository.findById(gameId);

		if (!possibleGame.isPresent())
			return "redirect:/play";
		
		model.addAttribute("content", "review/game/game");

		Game userGame = possibleGame.get();

		model.addAttribute("game", userGame);
		
		return "layout";
	}

	private Optional<String> getUserIdCookie(HttpServletRequest userRequest) {
		if(userRequest.getCookies() == null) return Optional.ofNullable(null);

		System.out.println("Checking Cookies");
		for (Cookie cookie : userRequest.getCookies()) {
			System.out.println(cookie.getValue());
			if ("linesOfActionUserId".equals(cookie.getName()))
				return Optional.ofNullable(cookie.getValue());
		}

		return Optional.ofNullable(null);
	}
}
