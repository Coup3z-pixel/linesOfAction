package com.play.linesOfAction.controller.play;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * GamePlayController
 */
@RestController
public class GamePlayController {

	@GetMapping("/play/game")
	public String redirectToGame(@RequestParam boolean isOnline) {
		if (isOnline) {
			return "/play/online";
		}

		return "/play/computer";
	}
}
