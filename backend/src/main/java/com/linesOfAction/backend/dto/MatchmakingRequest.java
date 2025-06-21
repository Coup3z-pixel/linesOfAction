package com.linesOfAction.backend.dto;

/**
 * MatchmakingRequestMessage
 */
public class MatchmakingRequest {

	private String userId;
	private int elo;
	private String recipient;

	MatchmakingRequest() {
	}

	public MatchmakingRequest(String userId, int elo, String recipient) {
		this.userId = userId;
		this.elo = elo;
		this.recipient = recipient;
	}

	public String getRecipient() {
		return this.recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getElo() {
		return this.elo;
	}

	public void setElo(int elo) {
		this.elo = elo;
	}
}
