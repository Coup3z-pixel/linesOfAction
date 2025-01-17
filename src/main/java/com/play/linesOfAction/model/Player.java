package com.play.linesOfAction.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User
 */
@Document(collection = "Players")
public class Player {

	@Id
	private String id;

	public Player(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ID: " + this.id;
	}
}
