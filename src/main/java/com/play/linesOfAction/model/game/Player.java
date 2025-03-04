package com.play.linesOfAction.model.game;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User
 */
@Document(collection = "Players")
public class Player {

	@Id
	private String id;
	private String sessionId;
	public String email;
	public String[] games; // stores the id of games

	public Player(String id, String sessionId, String email) {
		this.id = id;
		this.sessionId = sessionId;
		this.games = new String[]{};
		this.email = email;
	}

	public String getId() {
		return this.id;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public String getEmail() {
		return this.email;
	}

	@Override
	public String toString() {
		return "ID: " + this.id + "\nGames: " + this.games;
	}
}
