package com.linesOfAction.dto;

/**
 * MoveRequest
 */
public class MoveRequest {

	private String from;
	private String to;
	private String userId;

	public MoveRequest() {
	}

	public MoveRequest(String from, String to) {
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return this.from;
	}

	public String getTo() {
		return this.to;
	}

	public String getUserId() {
		return this.userId;
	}
}
