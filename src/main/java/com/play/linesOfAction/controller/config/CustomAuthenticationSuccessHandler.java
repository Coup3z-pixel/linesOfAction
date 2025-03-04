package com.play.linesOfAction.controller.config;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.play.linesOfAction.controller.db.PlayerRepository;
import com.play.linesOfAction.controller.db.PlayerTemplate;
import com.play.linesOfAction.model.game.Player;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CustomAuthenticationSuccessHandler
 */
@Component
public class CustomAuthenticationSuccessHandler 
	implements AuthenticationSuccessHandler {

		@Autowired
		PlayerRepository playerRepository;

		@Autowired
		PlayerTemplate playerTemplate;

		@Override
    public void onAuthenticationSuccess(
				HttpServletRequest request, 
				HttpServletResponse response, 
				Authentication authentication
			) throws IOException, ServletException {
	
				Object userObject = authentication.getPrincipal();
				OAuth2User user = (OAuth2User) userObject;

				UUID uuid = UUID.randomUUID();

				System.out.println(user);

				// Check if user with that email exists
				if(!playerTemplate.doesEmailExist(user.getAttribute("email"))) {
					playerRepository.save(new Player(uuid.toString(), "", user.getAttribute("email")));
				}

				response.sendRedirect("/play");
    }
}
