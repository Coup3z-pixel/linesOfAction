package com.linesOfAction.backend.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * LinesOfActionGame
 */
public class LinesOfActionGame {

	public enum Player { WHITE, BLACK };

	private Player[][] board;
	private Player currPlayer;
	private static int SIZE = 8;

	public LinesOfActionGame() {
		this.board = new Player[][]{
			{null, Player.BLACK, Player.BLACK, Player.BLACK, Player.BLACK, Player.BLACK, Player.BLACK, null},
			{Player.WHITE, null, null, null, null, null, null, Player.WHITE},
			{Player.WHITE, null, null, null, null, null, null, Player.WHITE},
			{Player.WHITE, null, null, null, null, null, null, Player.WHITE},
			{Player.WHITE, null, null, null, null, null, null, Player.WHITE},
			{Player.WHITE, null, null, null, null, null, null, Player.WHITE},
			{Player.WHITE, null, null, null, null, null, null, Player.WHITE},
			{null, Player.BLACK, Player.BLACK, Player.BLACK, Player.BLACK, Player.BLACK, Player.BLACK, null},
		};

		currPlayer = Player.WHITE;
	}

	public boolean move(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isInBounds(fromRow, fromCol) || !isInBounds(toRow, toCol)) return false;
        if (board[fromRow][fromCol] != currPlayer) return false;

        int dRow = Integer.compare(toRow, fromRow);
        int dCol = Integer.compare(toCol, fromCol);
        int distance = countPiecesInLine(fromRow, fromCol, dRow, dCol);

        // Check destination is at correct distance
        if (Math.abs(toRow - fromRow) != distance * Math.abs(dRow) ||
            Math.abs(toCol - fromCol) != distance * Math.abs(dCol)) {
            return false;
        }

        // Check path is clear or contains opponent at the end
        for (int i = 1; i < distance; i++) {
            int r = fromRow + i * dRow;
            int c = fromCol + i * dCol;
            if (board[r][c] != null) return false;
        }

        if (board[toRow][toCol] == currPlayer) return false;

        // Perform move
        board[toRow][toCol] = currPlayer;
        board[fromRow][fromCol] = null;

        switchTurn();
        return true;
    }

	public List<int[]> getPossibleMoves(int row, int col) {
		List<int[]> moves = new ArrayList<>();
		if (!isInBounds(row, col)) return moves;
		Player player = board[row][col];
		if (player == null) return moves;

		// Check all 8 directions
		for (int dr = -1; dr <= 1; dr++) {
			for (int dc = -1; dc <= 1; dc++) {
				if (dr == 0 && dc == 0) continue;

				int distance = countPiecesInLine(row, col, dr, dc);
				int targetRow = row + dr * distance;
				int targetCol = col + dc * distance;

				if (!isInBounds(targetRow, targetCol)) continue;
				Player target = board[targetRow][targetCol];

				// Can't land on own piece; can land on empty or opponent
				if (target == null || target != player) {
					// Ensure path is clear except final square
					boolean pathClear = true;
					for (int i = 1; i < distance; i++) {
						int r = row + dr * i;
						int c = col + dc * i;
						if (!isInBounds(r, c) || board[r][c] != null) {
							pathClear = false;
							break;
						}
					}
					if (pathClear) {
						moves.add(new int[]{targetRow, targetCol});
					}
				}
			}
		}

		return moves;
	}

    private int countPiecesInLine(int row, int col, int dRow, int dCol) {
        int count = 1; // include current piece

        for (int i = 1; isInBounds(row + i * dRow, col + i * dCol); i++) {
            if (board[row + i * dRow][col + i * dCol] != null) count++;
        }

        for (int i = 1; isInBounds(row - i * dRow, col - i * dCol); i++) {
            if (board[row - i * dRow][col - i * dCol] != null) count++;
        }

        return count;
    }

	public boolean hasPlayerWon(Player player) {
		boolean[][] visited = new boolean[SIZE][SIZE];
		int totalPieces = 0;
		int connectedPieces = 0;

		// Find total number of pieces and starting point for DFS
		int startRow = -1, startCol = -1;
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				if (board[r][c] == player) {
					totalPieces++;
					if (startRow == -1) {
						startRow = r;
						startCol = c;
					}
				}
			}
		}

		if (totalPieces == 0) return false;

		// DFS to count connected pieces
		Deque<int[]> stack = new ArrayDeque<>();
		stack.push(new int[]{startRow, startCol});
		visited[startRow][startCol] = true;

		while (!stack.isEmpty()) {
			int[] pos = stack.pop();
			int r = pos[0], c = pos[1];
			connectedPieces++;

			for (int dr = -1; dr <= 1; dr++) {
				for (int dc = -1; dc <= 1; dc++) {
					if (dr == 0 && dc == 0) continue;
					int nr = r + dr, nc = c + dc;
					if (isInBounds(nr, nc) && !visited[nr][nc] && board[nr][nc] == player) {
						visited[nr][nc] = true;
						stack.push(new int[]{nr, nc});
					}
				}
			}
		}

		return connectedPieces == totalPieces;
	}

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    private void switchTurn() {
        currPlayer = (currPlayer == Player.BLACK) ? Player.WHITE : Player.BLACK;
    }

    public Player getCurrentPlayer() {
        return currPlayer;
    }
}
