package com.linesOfAction.dto;

/**
 * MoveResponse
 */
public class MoveResponse {

	private String to;
	private String from;
	private boolean isSuccessful;

	public MoveResponse() {
	}

	public MoveResponse(String to, String from, boolean success) {
		this.to = to;
		this.from = from;
		this.isSuccessful = success;
	}

	public String getFrom() {
		return this.from;
	}

	public String getTo() {
		return this.to;
	}

	public boolean getSuccessful() {
		return this.isSuccessful;
	}
}
