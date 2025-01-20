package com.play.linesOfAction.controller.play;

import java.util.Deque;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.play.linesOfAction.model.game.Game;
import com.play.linesOfAction.model.game.GameReferee;
import com.play.linesOfAction.model.Player;
import com.play.linesOfAction.model.message.GameStatusMessage;
import com.play.linesOfAction.model.message.MoveMessage;

/**
 * OnlinePlayController
 */
@Controller
public class OnlinePlayController {

	private final ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();
	private final Deque<Player> playersWaiting = new ConcurrentLinkedDeque<>();
	private final GameReferee gameReferee = new GameReferee();

	@Autowired
	private SimpMessagingTemplate sendingOperations;

	@MessageMapping("/online") // url: /play/online
	public void playGame(MoveMessage move) {
		Game game = games.get(move.getGameId());
		short result = gameReferee.getGameState(game);


		if(game == null) return;
		if(result != -1) {
			// Save it on MongoDB
			// Send the information to both users
			System.out.println("HI");
			
			return;
		}

		boolean isMoveValid = gameReferee.isMoveValid(game, move.getFrom(), move.getTo(), move.getPlayerIndex());

		if (isMoveValid) {
			game.movePiece(move.getFrom(), move.getTo());

			sendingOperations.convertAndSendToUser(
					game.getPlayer(1-move.getPlayerIndex()).getId(), 
					"/move/online", 
					new MoveMessage("", move.getFrom(), move.getTo(), (short)-1)
				);

			System.out.println(game);
			System.out.println(result);

			return;
		}
	
		sendingOperations.convertAndSendToUser(
				game.getPlayer(move.getPlayerIndex()).getId(), 
				"/move/online", 
				false // TODO game error message
			);

		return;
	}

	@GetMapping("/play/online")
	public String onlinePage(Model model) {
		model.addAttribute("content", "play/online/online");
		return "layout";
	}

	// WebSocket for init the game
	@MessageMapping("/start") // url: /play/start
	public void notifyGameStart(
			Player player,
			SimpMessageHeaderAccessor headerAccessor
		) {

		Player newPlayer = new Player(player.getId());
		headerAccessor.getSessionAttributes().put("player", newPlayer);	

		Optional<Player> potentialPlayer = this.getAvailablePlayer();

		if (potentialPlayer.isPresent() && !potentialPlayer.get().equals(newPlayer)) {
			UUID gameId = UUID.randomUUID();

			Game newGame = new Game(
				UUID.randomUUID().toString(),
				newPlayer,
				potentialPlayer.get()
			);

			games.put(gameId.toString(), newGame);

			sendingOperations.convertAndSendToUser(
					potentialPlayer.get().getId(), 
					"/move/start", 
					new GameStatusMessage("Starting", (short)1, gameId.toString())
				);

			sendingOperations.convertAndSendToUser(
					newPlayer.getId(), 
					"/move/start", 
					new GameStatusMessage("Starting", (short)0, gameId.toString())
				);

			return;
		}

		playersWaiting.addLast(newPlayer);

		sendingOperations.convertAndSendToUser(
				player.getId(), 
				"/move/start", 
				new GameStatusMessage("Waiting", (short)-1, null)
			);

		return;
	}

	private Optional<Player> getAvailablePlayer() {
		return Optional.ofNullable(playersWaiting.pollFirst());
	}
}
