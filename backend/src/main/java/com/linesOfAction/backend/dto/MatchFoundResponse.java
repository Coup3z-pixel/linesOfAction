package com.linesOfAction.backend.dto;

/**
 * MatchFoundResponse
 */
public class MatchFoundResponse {

	private String gameId;

	public MatchFoundResponse() {}

	public MatchFoundResponse(String gameId) {
		this.gameId = gameId;
	}

	public String getGameId() {
		return this.gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
