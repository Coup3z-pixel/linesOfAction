package com.linesOfAction.backend.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.linesOfAction.backend.model.LinesOfActionGame;

/**
 * GameService
 */
@Service
public class GameService {

	private final ConcurrentHashMap<String, LinesOfActionGame> currGames = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, String> playerToGameId = new ConcurrentHashMap<>();

	public GameService() {}

	public void initGame(String gameId, String playerOneId, String playerTwoId) {
		currGames.put(gameId, new LinesOfActionGame());

		playerToGameId.put(playerOneId, gameId);
		playerToGameId.put(playerTwoId, gameId);
	}

	public LinesOfActionGame retrieveGame(String gameId) {
		return currGames.get(gameId);
	}
}
