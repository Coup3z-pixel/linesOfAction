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

	public Player(String id, String sessionId) {
		this.id = id;
		this.sessionId = sessionId;
	}

	public String getId() {
		return this.id;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	@Override
	public String toString() {
		return "ID: " + this.id;
	}
}
