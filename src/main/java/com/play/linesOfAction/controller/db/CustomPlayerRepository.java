package com.play.linesOfAction.controller.db;

/**
 * CustomPlayerRepository
 */
public interface CustomPlayerRepository { 
	void pushGame(String playerId, String gameId);
	Object getGames(String playerId);
}
