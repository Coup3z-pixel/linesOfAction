package com.play.linesOfAction.model.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.checkerframework.checker.units.qual.s;

/**
 * GameReferee
 */
public class GameReferee {
	
	public GameReferee() {}

	/* Returns the Player who won or -1 for noone */
	public int getGameState(Game game) {

		boolean playerOneWin = this.areAllPiecesConnected(
				game, 
				game.getPieceOfPlayer((short)0)
			);

		boolean playerTwoWin = this.areAllPiecesConnected(
				game, 
				game.getPieceOfPlayer((short)1)
			);

		if (playerOneWin) return 0;
		if (playerTwoWin) return 1;

		return -1;
	}

	public boolean areAllPiecesConnected(Game game, char playerPiece) {
		int totalPiece = 0;
		int xPos = 0;
		int yPos = 0;
		boolean startFound = false;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (game.board[i][j] == playerPiece) {
					totalPiece++;
					if (!startFound) {
						xPos = j;
						yPos = i;
						startFound = true;
					}
				}
			}
		}

		int count = this.bfs(game, xPos, yPos, playerPiece);

		return totalPiece == count;
	}

	private int bfs(Game game, int xPos, int yPos, char playerPiece) {

		Queue<Integer> q = new LinkedList<>();
		Set<Integer> visited = new HashSet<>();
		int[][] directions = {{1,1}, {1,0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};

		q.add(yPos * 8 + xPos);

		while (!q.isEmpty()) {
			int cord = q.poll();
			int row = cord / 8, col = cord % 8;

			for (int[] direction : directions) {
					int nr = row + direction[0], nc = col + direction[1];
					if (nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && game.board[nr][nc] == playerPiece && !visited.contains(nr * 8 + nc)) {
							q.add(nr * 8 + nc);
							visited.add(nr * 8 + nc);
					}
			}
		}

		return visited.size();
  }

	public boolean isMoveValid(Game game, String init, String move, short playerIndex) {

		int[] initCord = game.convertStrToCord(init);
		int[] finalCord = game.convertStrToCord(move);

		// board[yCord][xCord]
		
		// Tested
		if(
			!game.isInRange(initCord[0]) || !game.isInRange(initCord[1]) ||
			!game.isInRange(finalCord[0]) || !game.isInRange(finalCord[1])
		) {
			System.out.println("Move is not in the range of the board");
			return false;
		}

		// Tested
		if(!game.isPieceOfPlayer(initCord[1], initCord[0], playerIndex)) {
			System.out.println("Not Player's Piece");
			return false;
		}

		int xDisplacement = finalCord[1] - initCord[1];
		int yDisplacement = finalCord[0] - initCord[0];
	
		boolean moveStraight = (xDisplacement == 0) || (yDisplacement == 0);
		boolean moveDiagonal = (Math.abs(xDisplacement) == Math.abs(yDisplacement));

		if (!moveStraight && !moveDiagonal) {
			System.out.println("Move is not straight or diagonal");
			return false;
		} 

		// Check line of action equals max(xDisplacement, yDisplacement)
		int piecesOnLine = -1; // -1 due to the recount of the piece
		int xDirection = 0;
		int yDirection = 0;

		if (xDisplacement == 0) xDirection = 0;
		else xDirection = xDisplacement / Math.abs(xDisplacement);

		if (yDisplacement == 0) yDirection = 0;
		else yDirection = yDisplacement / Math.abs(yDisplacement);

		int x = initCord[1];
		int y = initCord[0];

		while (game.isInRange(x) && game.isInRange(y)) {
			if (game.board[y][x] != ' ') piecesOnLine++;
			x += xDirection; y += yDirection;
		}

		x = initCord[1];
		y = initCord[0];

		while (game.isInRange(x) && game.isInRange(y)) {
			if (game.board[y][x] != ' ') piecesOnLine++;
			x -= xDirection; y -= yDirection;
		}

		if (piecesOnLine != Math.max(Math.abs(xDisplacement), Math.abs(yDisplacement))) {
			System.out.println("Move is too close or too far");
			return false;
		}

		int xCount = initCord[1] + xDirection;
		int yCount = initCord[0] + yDirection;		

		while(yCount != finalCord[0] || xCount != finalCord[1]) {
			if(game.board[yCount][xCount] == game.getPieceOfPlayer((short)(1-playerIndex))) {
				System.out.println("Move goes over enemy piece");
				return false;
			}

			System.out.println(game.board[yCount][xCount]);
			System.out.println(game.getPieceOfPlayer((short)(1-playerIndex)));

			xCount += xDirection;
			yCount += yDirection;
		}

		return true;
	}

}
