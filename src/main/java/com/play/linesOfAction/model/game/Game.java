package com.play.linesOfAction.model.game;

import java.util.Iterator;
import java.util.LinkedList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.play.linesOfAction.model.Player;

/**
 * Game
**/
@Document(collection = "Games")
public class Game {

	@Id
	private String id;
	public char[][] board;
	public LinkedList<String[]> history;
	private Player[] players;
	private int currentPlayerTurn;

	public Game(String id, Player one, Player two) {
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

		history = new LinkedList<String[]>();

		this.players = new Player[]{one, two};
	}

	public Game(char[][] board) { this.board = board; }

	public String getId() { return this.id; }
	public Player getPlayer(int playerIndex) { return this.players[playerIndex]; }

	public Game returnGame(int moves) {
		Game recount = new Game("", this.players[0], this.players[1]);
		Iterator<String[]> iterator = this.history.iterator();

		for (int i = 0; i < moves; i++) {
			if (!iterator.hasNext()) return recount;

			String[] move = iterator.next();
			recount.movePiece(move[0], move[1]);
		}

		return recount;
	}

	public short returnPlayerIndex(String user_id) {
		if(this.players[0].getId().equals(user_id)) return (short)0;
		if(this.players[0].getId().equals(user_id)) return (short)1;
		return (short)-1;
	}

	public void pushHistory(String init, String move) {
		history.add(new String[]{init, move});
	}

	public char getPieceOfPlayer(short playerIndex) {
		if (playerIndex == 0) return 'w';
		else return 'b';
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

		this.currentPlayerTurn = 1 - this.currentPlayerTurn;
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
