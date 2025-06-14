package com.linesOfAction.backend.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * LinesOfActionGameTest
 */
public class LinesOfActionGameTest {

	private LinesOfActionGame game;

	@Test
	void move() {
	}

	@Test
	void isInBound() {
	}

	@Test
	void hasWinningPosition() {

	}

	@BeforeAll	
	void setup() {
		this.game = new LinesOfActionGame();
	}

	@AfterAll
	void cleanup() {
		this.game = null;
	}
}
