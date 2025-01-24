package com.play.linesOfAction.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * SecurityConfig
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests( auth -> {
				auth
					.requestMatchers("/user/**").authenticated()
					.anyRequest().permitAll();
			})
		.oauth2Login(withDefaults())
		.getOrBuild();
	}

	@Bean
	public AuthenticationSuccessHandler customSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}
}
