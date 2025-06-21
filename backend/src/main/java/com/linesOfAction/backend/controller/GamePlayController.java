package com.linesOfAction.backend.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.linesOfAction.backend.model.LinesOfActionGame;
import com.linesOfAction.backend.service.GameService;
import com.linesOfAction.backend.dto.MoveRequest;
import com.linesOfAction.backend.dto.MoveResponse;


/**
 * GamePlayController
 */
@Controller
@RequestMapping("/game")
public class GamePlayController {

	private final GameService gameService;

	public GamePlayController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/info/{gameId}")
	public void getGameInfo(@PathVariable(value = "gameId") String id) {
		System.out.println(id);
	}


	@MessageMapping("/move")
	@SendTo("/match-info/move-status")
	public MoveResponse matchmaking(MoveRequest moveRequest) throws Exception {
		return null;
	}

	private List<int[]> getAvailabeMoves(LinesOfActionGame linesOfActionGame, MoveRequest moveRequest) {
		int row = moveRequest.getFrom() % 8;
		int col = Math.floorDiv(moveRequest.getFrom(), 8);	

		return linesOfActionGame.getPossibleMoves(row, col);
	}
}
