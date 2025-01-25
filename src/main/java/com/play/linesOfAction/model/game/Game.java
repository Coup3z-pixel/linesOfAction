package com.play.linesOfAction.model.game;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Game
**/
@Document(collection = "Games")
public class Game {

	private String id;
	public char[][] board;
	public GameHistory history;
	private String[] players;
	private short currentPlayerTurn;

	public Game(String id, String oneId, String twoId) {
		this.board = new char[][]{
			{' ', 'w', 'w', 'w', 'w', 'w', 'w', ' '},
			{'b', ' ', ' ', ' ', ' ', ' ', ' ', 'b'},
			{'b', ' ', ' ', ' ', ' ', ' ', ' ', 'b'},
			{'b', ' ', ' ', ' ', ' ', ' ', ' ', 'b'},
			{'b', ' ', ' ', ' ', ' ', ' ', ' ', 'b'},
			{'b', ' ', ' ', ' ', ' ', ' ', ' ', 'b'},
			{'b', ' ', ' ', ' ', ' ', ' ', ' ', 'b'},
			{' ', 'w', 'w', 'w', 'w', 'w', 'w', ' '},
		};
		
		this.history = new GameHistory();

		this.players = new String[]{oneId, twoId};
		currentPlayerTurn = 0;
	}

	public Game(char[][] board) { this.board = board; }

	public String getId() { return this.id; }
	public String getPlayerId(int playerIndex) { return this.players[playerIndex]; }
	public short getCurrTurn() { return this.currentPlayerTurn; }

	public short returnPlayerIndex(String user_id) {
		if(this.players[0].equals(user_id)) return (short)0;
		if(this.players[0].equals(user_id)) return (short)1;
		return (short)-1;
	}

	public char getPieceOfPlayer(short playerIndex) {
		return (playerIndex == 0 ? 'w' : 'b');
	}

	// Returns yCord, xCord
	public int[] convertStrToCord(String cord) {
		int yCord = cord.charAt(1) - 49;
		int xCord = cord.charAt(0) - 97;

		return new int[]{7-yCord,xCord};
	}

	public void movePiece(String init, String move) {
		int[] initCord = this.convertStrToCord(init);
		int[] finalCord = this.convertStrToCord(move);
	
		char piece = this.board[initCord[0]][initCord[1]];
		this.board[initCord[0]][initCord[1]] = ' ';
		this.board[finalCord[0]][finalCord[1]] = piece;

		this.currentPlayerTurn = (short)(1 - this.currentPlayerTurn);
		this.history.pushHistory(init, move);
	}

	/* Checks if the boards[yCord][xCord] is playerIndex's */
	public boolean isPieceOfPlayer(int xCord, int yCord, short playerIndex) {
		char playerPiece = this.getPieceOfPlayer(playerIndex);

		if(this.board[yCord][xCord] != playerPiece)
			return false;

		return true;
	}

	public boolean isInRange(int cord) {
		return 0 <= cord && cord <= 7;
	}

	@Override
	public String toString() {
		String board = "";
		for (int i = 0; i < this.board.length; i++) {
			String row = "";
			for (int j = 0; j < this.board[i].length; j++) {
				row += this.board[i][j];
			}
			board = board + row + "\n";
		}

		return board;
	}
}
