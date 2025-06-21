package com.linesOfAction.backend.dto;

/**
 * MoveRequest
 */
public class MoveRequest {

	private int from;
	private int to;
	private String userId;

	public MoveRequest() {
	}

	public MoveRequest(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public int getFrom() {
		return this.from;
	}

	public int getTo() {
		return this.to;
	}

	public String getUserId() {
		return this.userId;
	}
}
