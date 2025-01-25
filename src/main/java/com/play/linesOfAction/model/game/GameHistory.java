package com.play.linesOfAction.model.game;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * GameHistory
 */
public class GameHistory {
	
	private LinkedList<String[]> history;
	private String[] players;

	public GameHistory() {
		this.history = new LinkedList<>();
	}

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

	public void pushHistory(String init, String move) {
		history.add(new String[]{init, move});
	}
}
