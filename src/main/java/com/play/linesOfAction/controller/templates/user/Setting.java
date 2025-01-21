package com.play.linesOfAction.controller.templates.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SettingController
 */
@Controller
public class Setting {

	@GetMapping("/user/setting")
	public String homePage() {
		return "setting/setting";
	}
}
