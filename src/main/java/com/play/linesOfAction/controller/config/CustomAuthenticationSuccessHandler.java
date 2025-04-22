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
import jakarta.servlet.http.Cookie;
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

				// Check if user with that email exists
				if(!playerTemplate.doesEmailExist(user.getAttribute("email")))
					playerRepository.save(new Player(uuid.toString(), "", user.getAttribute("email")));
				
				Cookie userIdCookie = new Cookie("linesOfActionUserId", uuid.toString());

        userIdCookie.setHttpOnly(true);  // Prevent JavaScript access
        userIdCookie.setSecure(true);     // Only allow on HTTPS
        userIdCookie.setPath("/user");        // Available for the entire site
        userIdCookie.setMaxAge(60 * 60 * 24 * 7); // 1 week expiration
				response.addCookie(userIdCookie);

				response.sendRedirect("/play");
    }
}
