package com.linesOfAction.dto;

/**
 * MatchmakingMessage
 */
public class MatchmakingResponse {

	private int playersWaiting;
	private boolean found;

	MatchmakingResponse() {
	}

	public MatchmakingResponse(int playersWaiting, boolean found) {
		this.playersWaiting = playersWaiting;
		this.found = found;
	}

	public int getPlayersWaiting() {
		return this.playersWaiting;
	}

	public void setPlayersWaiting(int playersWaiting) {
		this.playersWaiting = playersWaiting;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public boolean getFound() {
		return this.found;
	}
}
