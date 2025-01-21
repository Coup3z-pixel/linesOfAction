package com.play.linesOfAction;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.play.linesOfAction.model.game.Player;
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
		Game gameTest = new Game("", new Player("", ""), new Player("", ""));
		GameReferee gameReferee = new GameReferee();
		assertTrue(gameReferee.isMoveValid(gameTest, "b1", "b3", (short)0));
		gameTest.movePiece("b1", "b3");
		assertTrue(!gameReferee.isMoveValid(gameTest, "a3", "d3", (short)1));
	}

	@Test
	void diagonalMovement() {
		Game gameTest = new Game("", new Player("", ""), new Player("", ""));
		GameReferee gameReferee = new GameReferee();
		System.out.println(gameTest);
		assertTrue(!gameReferee.isMoveValid(gameTest, "b8", "d6", (short)0));
		gameTest.movePiece("b8", "d6");
		System.out.println(gameTest);
	}

	@Test
	void gameState() {
		char[][] board = new char[][]{
			{' ', 'b', 'b', 'b', 'b', 'b', 'b', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', 'w', 'w'},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', 'b', 'b', 'b', 'b', 'b', 'b', ' '},
		};

		Game gameTest = new Game(board);
		GameReferee gameReferee = new GameReferee();

		assertTrue(gameReferee.getGameState(gameTest) == 0);
	}
}
