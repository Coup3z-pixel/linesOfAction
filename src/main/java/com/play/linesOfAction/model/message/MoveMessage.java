package com.play.linesOfAction.model.message;

/**
 * MoveMessage
 */
public class MoveMessage {

	private String gameId;
	private String from;
	private String to;
	private short playerIndex;

	public MoveMessage() {}
	public MoveMessage(String gameId, String from, String to, short playerIndex) {
		this.gameId = gameId;
		this.from = from;
		this.to = to;
		this.playerIndex = playerIndex;
	}

	public String getGameId() { return this.gameId; }
	public String getFrom() { return this.from; }
	public String getTo() { return this.to; }
	public short getPlayerIndex() { return this.playerIndex; }

	public void setGameId(String gameId) { this.gameId = gameId; }
	public void setFrom(String from) { this.from = from; }
	public void setTo(String to) { this.to = to; }
	public void setPlayerIndex(short playerIndex) { this.playerIndex = playerIndex; }

	@Override
	public String toString() {
		return "GameID: " + this.gameId + "\n"+"From: " + this.from + "\n" + "To: " + this.to + "\n" + "Index: " + this.playerIndex;
	}
}
