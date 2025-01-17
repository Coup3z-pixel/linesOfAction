package com.play.linesOfAction.controller.templates;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HomeController
 */
@Controller
public class Home {

	@GetMapping("/home")
	public String homePage(Model model) {
		model.addAttribute("content", "home/home");
		return "layout";
	}
}
