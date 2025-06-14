package com.linesOfAction.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/match-info", "/user"); // player receives from
		config.setApplicationDestinationPrefixes("/match-action"); // sends msg to
        config.setUserDestinationPrefix("/user"); // backend sends to /user/{username}/...
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/game-play")
			.setAllowedOriginPatterns("http://localhost:5173")
			.withSockJS();
  }
}
