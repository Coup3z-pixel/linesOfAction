package com.linesOfAction.backend.controller;

import java.security.Principal;
import java.util.Deque;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.linesOfAction.backend.dto.MatchmakingRequest;
import com.linesOfAction.backend.dto.MatchmakingResponse;

/**
 * MatchmakingController
 */
@Controller
public class MatchmakingController {

	@Autowired
	private SimpMessagingTemplate messageTemplate;

	private Deque<WaitingUser> matchQueue = new ConcurrentLinkedDeque<>();

	@MessageMapping("/find")
	public void matchmakingRequest(MatchmakingRequest matchRequestMessage, Principal principal) throws Exception {
		System.out.println("New Player: " + matchRequestMessage.getRecipient() + " has joined");

		this.matchQueue.add(new WaitingUser(matchRequestMessage.getRecipient(), matchRequestMessage.getElo()));

		if(matchQueue.size() % 2 == 0 && matchQueue.size() > 0) {
			WaitingUser firstUser = matchQueue.pollFirst();
			WaitingUser secondUser = matchQueue.pollFirst();

			UUID gameId = UUID.randomUUID();

			messageTemplate.convertAndSendToUser(
					firstUser.getUserId(), 
					"/match-info/connect", 
					new MatchmakingResponse(matchQueue.size(), true)
			);

			messageTemplate.convertAndSendToUser(
					secondUser.getUserId(), 
					"/match-info/connect", 
					new MatchmakingResponse(matchQueue.size(), true)
				);

			System.out.println(
					"Game started with: " 
						+ firstUser.getUserId() 
						+ " and " 
						+ secondUser.getUserId()
				);
		}

		messageTemplate.convertAndSend(
				"/match-info/queue-size",
				new MatchmakingResponse(matchQueue.size(), false)
			);
	}

	@MessageMapping("/ask")
	@SendTo("/match-info/queue-size")
	public MatchmakingResponse matchmakingQueueSize(@Payload String body) throws Exception {
		return new MatchmakingResponse(matchQueue.size(), false);
	}

	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent event) {
		System.out.println(event.getSessionId());
	}
	
	private String[] matchUser() {
		return new String[]{};
	}

	private class WaitingUser {
		private String userId;
		private int elo;

		public WaitingUser(String userId, int elo) {
			this.userId = userId;
			this.elo = elo;
		}

		public String getUserId() {
			return this.userId;
		}

		public int getElo() {
			return this.elo;
		}
	}
}
