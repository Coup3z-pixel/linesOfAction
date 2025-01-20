package com.play.linesOfAction.controller.play;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ComputerPlayController
 */
@Controller
public class ComputerPlayController {

	@GetMapping("/play/computer")
	public String computerPage() {
		return "/play/computer";
	}
}
