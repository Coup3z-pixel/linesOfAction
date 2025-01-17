package com.play.linesOfAction.controller.templates;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SignController
 */
@Controller
public class Sign {

	@GetMapping("/sign")
	public String homePage() {
		return "sign/sign";
	}
}
