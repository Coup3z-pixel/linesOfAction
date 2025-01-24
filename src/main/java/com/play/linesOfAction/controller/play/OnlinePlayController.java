package com.play.linesOfAction.controller.play;

import java.util.Deque;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.play.linesOfAction.controller.db.GameRepository;
import com.play.linesOfAction.model.game.Game;
import com.play.linesOfAction.model.game.GameReferee;
import com.play.linesOfAction.model.game.Player;
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

	@Autowired
	GameRepository gameRepository;

	@MessageMapping("/online") // url: /play/online
	public void playGame(MoveMessage move) {
		Game game = games.get(move.getGameId());
		short result = gameReferee.getGameState(game);

		System.out.println(move);

		if(game == null) return;
		if(result != -1) {
			gameRepository.save(game);
			games.remove(move.getGameId());

			// Notify users

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
				false
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

		Player newPlayer = new Player(player.getId(), headerAccessor.getSessionId(), "");	

		Optional<Player> potentialPlayer = this.getAvailablePlayer();

		if (potentialPlayer.isPresent() && !potentialPlayer.get().equals(newPlayer)) {
			UUID gameId = UUID.randomUUID();

			Game newGame = new Game(
				gameId.toString(),
				newPlayer,
				potentialPlayer.get()
			);

			games.put(gameId.toString(), newGame);
			headerAccessor.getSessionAttributes().put("game", gameId.toString());	

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

	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent event) {
		// Check for if they were in a game
		
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String gameId = (String) headerAccessor.getSessionAttributes().get("game");

		// if player is in a game
		if(gameId != null) {
			Game userGame = this.games.get(gameId);

			// Debug this
			this.sendingOperations.convertAndSendToUser(
					userGame.getPlayer(0).getId(), 
					"/move/online", 
					new MoveMessage()
				);

			this.sendingOperations.convertAndSendToUser(
					userGame.getPlayer(1).getId(), 
					"/move/online", 
					new MoveMessage()
				);

			return;
		}
		
		this.removePlayer(event.getSessionId());
		return;
	}

	private Optional<Player> getAvailablePlayer() {
		return Optional.ofNullable(playersWaiting.pollFirst());
	}

	private void removePlayer(String id) {
		this.playersWaiting.removeIf(
				player -> (player.getSessionId().equals(id))
			);
	}
}
