package com.play.linesOfAction.controller.templates;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Play
 */
@Controller
public class Play {

	@GetMapping("/play")
	public String playPage(Model model) {
		model.addAttribute("content", "play/play");
		return "layout";
	}

}
