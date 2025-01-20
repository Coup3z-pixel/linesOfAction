package com.play.linesOfAction.model.message;

/**
 * MoveErrorMessage
 */
public class MoveErrorMessage {

	private String reason;

	public MoveErrorMessage() {
	}

	public MoveErrorMessage(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
