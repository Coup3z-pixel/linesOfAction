package com.play.linesOfAction.controller.templates;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HomeController
 */
@Controller
public class Index {

	@GetMapping("/")
	public String homePage() {
		return "index";
	}

	@GetMapping("/about")
	public String aboutPage() {
		return "about";
	}

	@GetMapping("/service")
	public String servicePage() {
		return "service";
	}
}
