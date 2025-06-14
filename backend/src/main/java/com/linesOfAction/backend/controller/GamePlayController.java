package com.linesOfAction.backend.controller;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.linesOfAction.backend.model.LinesOfActionGame;
import com.linesOfAction.backend.dto.MoveRequest;
import com.linesOfAction.backend.dto.MoveResponse;


/**
 * GamePlayController
 */
@Controller
public class GamePlayController {

	private ConcurrentHashMap<String, LinesOfActionGame> currGames = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, String> playerToGameId = new ConcurrentHashMap<>();

	@MessageMapping("/move")
	@SendTo("/match-info/move-status")
	public MoveResponse matchmaking(MoveRequest moveRequest) throws Exception {
		String gameId = playerToGameId.get(moveRequest.getUserId());

		LinesOfActionGame playerGame = currGames.get(gameId);

		String from = moveRequest.getFrom();
		String to = moveRequest.getTo();

		boolean moveSuccessful = playerGame.move(
				from.charAt(1), 
				from.charAt(0) - 97, 
				to.charAt(1), 
				to.charAt(0) - 97
			);


		return new MoveResponse(
				moveRequest.getFrom(),
				moveRequest.getTo(), 
				moveSuccessful
			);
	}
}
