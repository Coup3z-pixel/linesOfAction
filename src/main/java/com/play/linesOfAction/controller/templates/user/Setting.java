package com.play.linesOfAction.controller.templates.user;

import java.security.Principal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SettingController
 */
@Controller
public class Setting {

	@GetMapping("/user/setting")
	public String settingPage(@AuthenticationPrincipal Principal principal, Model model) {
		model.addAttribute("content", "setting/setting");
		return "layout";
	}
}
