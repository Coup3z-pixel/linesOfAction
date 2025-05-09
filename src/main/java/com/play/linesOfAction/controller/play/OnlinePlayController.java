package com.play.linesOfAction.controller.play;

import java.util.Deque;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.play.linesOfAction.controller.db.GameRepository;
import com.play.linesOfAction.controller.db.PlayerTemplate;
import com.play.linesOfAction.model.game.Game;
import com.play.linesOfAction.model.game.GameReferee;
import com.play.linesOfAction.model.game.Player;
import com.play.linesOfAction.model.message.GameStatusMessage;
import com.play.linesOfAction.model.message.MoveMessage;
import com.play.linesOfAction.model.message.PlayerCount;

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

	@Autowired
	PlayerTemplate playerTemplate;	

	@SendTo("/move/lobby")
	public PlayerCount playerWaitingCount(WebSocketSession session) {
		System.out.println("User Session Attr: " + session.getAttributes());
		System.out.println("Num of Players Waiting: " + this.playersWaiting.size());
		return new PlayerCount(this.playersWaiting.size());
	}

	@MessageMapping("/online") // url: /play/online
	public void playGame(MoveMessage move, WebSocketSession session) {

		System.out.println(session.getAttributes());

		Game game = this.games.get(move.getGameId());	
		if(game == null) return;

		short result = this.gameReferee.getGameState(game);
		if(result != -1) {
			this.gameRepository.save(game);	

			this.playerTemplate.pushGame(
					game.getPlayerId(0), 
					game.getId()
				);

			this.playerTemplate.pushGame(
					game.getPlayerId(1), 
					game.getId()
				);
			
			this.games.remove(move.getGameId());

			// Notify users
			sendingOperations.convertAndSendToUser(
					game.getPlayerId(result), 
					"/move/online", 
					new GameStatusMessage("won", result, game.getId())
				);

			sendingOperations.convertAndSendToUser(
					game.getPlayerId(1-result), 
					"/move/online", 
					new GameStatusMessage("lost", result, game.getId())
				);

			return;
		}

		boolean isMoveValid = this.gameReferee.isMoveValid(game, move.getFrom(), move.getTo(), move.getPlayerIndex());

		if (isMoveValid) {
			game.movePiece(move.getFrom(), move.getTo());

			sendingOperations.convertAndSendToUser(
					game.getPlayerId(1-move.getPlayerIndex()), // get opponents player index
					"/move/online", 
					new MoveMessage("", move.getFrom(), move.getTo(), (short)-1)
				);

			return;
		}
	
		sendingOperations.convertAndSendToUser(
				game.getPlayerId(move.getPlayerIndex()), 
				"/move/online", 
				false
			);

		return;
	}

	// WebSocket for init the game
	@MessageMapping("/start") // url: /play/start
	public void notifyGameStart(
			Player player,
			SimpMessageHeaderAccessor headerAccessor
		) {

		Player newPlayer = new Player(player.getId(), headerAccessor.getSessionId(), "");	

		Optional<Player> potentialPlayer = this.getAvailablePlayer();

		if (potentialPlayer.isPresent() && !potentialPlayer.get().getId().equals(newPlayer.getId())) {
			UUID gameId = UUID.randomUUID();

			Game newGame = new Game(
				gameId.toString(),
				newPlayer.getId(),
				potentialPlayer.get().getId()
			);

			this.games.put(gameId.toString(), newGame);
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

			
			// After setting game new size is published
			sendingOperations.convertAndSend(
					"/move/lobby", 
					new PlayerCount(this.playersWaiting.size())
				);

			return;
		}

		this.playersWaiting.addLast(newPlayer);

		sendingOperations.convertAndSendToUser(
				player.getId(), 
				"/move/start", 
				new GameStatusMessage("Waiting", (short)-1, null)
			);

		// After new player is in queue
		sendingOperations.convertAndSend(
				"/move/lobby", 
				new PlayerCount(this.playersWaiting.size())
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
					userGame.getPlayerId(0), 
					"/move/online", 
					new MoveMessage()
				);

			this.sendingOperations.convertAndSendToUser(
					userGame.getPlayerId(1), 
					"/move/online", 
					new MoveMessage()
				);

			return;
		}
		
		this.removePlayer(event.getSessionId());
		return;
	}

	private Optional<Player> getAvailablePlayer() {
		return Optional.ofNullable(this.playersWaiting.pollFirst());
	}

	private void removePlayer(String id) {
		this.playersWaiting.removeIf(
				player -> (player.getSessionId().equals(id))
			);
	}

	@GetMapping("/play/online")
	public String onlinePage(Model model) {
		model.addAttribute("content", "play/online/online");
		return "layout";
	}
}
