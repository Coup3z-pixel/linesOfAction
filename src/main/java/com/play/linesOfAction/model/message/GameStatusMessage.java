package com.play.linesOfAction.model.message;

/**
 * GameStatusMessage
 */
public class GameStatusMessage {

	private String gameId;
	private String status;
	private short index;

	public GameStatusMessage() {}

	public GameStatusMessage(String status, short index, String gameId) {
		this.status = status;
		this.index = index;
		this.gameId = gameId;
	}

	public void setStatus(String status) { this.status = status; }
	public void setPlayerIndex(short index) { this.index = index; }
	public void setGameId(String gameId) { this.gameId = gameId; }

	public String getStatus() { return this.status; }
	public short getPlayerIndex() { return this.index; }
	public String getGameId() { return this.gameId; }

}
