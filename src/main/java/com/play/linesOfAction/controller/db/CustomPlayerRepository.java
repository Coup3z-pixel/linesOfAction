package com.play.linesOfAction.controller.db;

import java.util.List;

import com.play.linesOfAction.model.game.Game;

/**
 * CustomPlayerRepository
 */
public interface CustomPlayerRepository { 
	void pushGame(String playerId, String gameId);
	Object getIdOfGames(String playerId);
	List<Game> getGames(List<String> idOfGames);
}
