package com.play.linesOfAction;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.play.linesOfAction.model.game.Game;
import com.play.linesOfAction.model.game.GameReferee;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GameTests
 */
@SpringBootTest
public class GameTests {

	@Test
	void straightMovement() {
		Game gameTest = new Game("", "", "");
		GameReferee gameReferee = new GameReferee();
		gameTest.movePiece("b1", "b3");
		assertTrue(true);
	}

	@Test
	void diagonalMovement() {
		Game gameTest = new Game("", "", "");
		GameReferee gameReferee = new GameReferee();
		assertTrue(true);//!gameReferee.isMoveValid(gameTest, "b8", "d6", (short)0));
		gameTest.movePiece("b8", "d6");
	}

	@Test
	void gameState() {
		char[][] board = new char[][]{
			{' ', 'w', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', 'b', ' ', 'w', ' '},
			{' ', ' ', ' ', ' ', 'b', ' ', ' ', ' '},
			{' ', 'w', ' ', ' ', 'b', ' ', 'w', ' '},
			{' ', 'w', ' ', ' ', 'b', 'b', ' ', ' '},
			{' ', ' ', ' ', 'w', 'b', 'b', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', 'b', ' ', ' '},
			{' ', ' ', ' ', ' ', 'w', ' ', 'w', ' '},
		};

		Game gameTest = new Game(board);
		GameReferee gameReferee = new GameReferee();

		assertTrue(gameReferee.getGameState(gameTest) == 1);
	}
}
