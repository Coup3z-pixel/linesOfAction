package com.play.linesOfAction.controller.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.play.linesOfAction.model.game.Game;
import com.play.linesOfAction.model.game.Player;

/**
 * PlayerTemplate
 */
@Repository
public class PlayerTemplate implements CustomPlayerRepository {

	@Autowired
	GameRepository gameRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public void pushGame(String playerId, String gameId) {
		Query query = new Query();
		query.addCriteria(new Criteria("_id").is(playerId));

		Update update = new Update().push("games", gameId);

		mongoTemplate.updateFirst(query, update, Player.class);
	}

	@Override
	public List<String> getIdOfGames(String playerId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(playerId));
		query.fields().include("games");

		Document games = mongoTemplate.findOne(query, Document.class, "Players");

		if (games != null) {
			return games.getList("games", String.class);
		}

		return null;
	}

	@Override
	public List<Game> getGames(List<String> idOfGames) {
		List<Game> games = new ArrayList<>();

		for (int i = 0; i < idOfGames.size(); i++) {
			Optional<Game> optionalGame =  gameRepository.findById(idOfGames.get(i));

			if (optionalGame.isPresent())
				games.add(optionalGame.get());
		}

		return games;
	}

	@Override
	public boolean isGameInUserHistory(String playerId, String gameId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(playerId));
		query.addCriteria(Criteria.where("games").in(gameId));

		return mongoTemplate.exists(query, Player.class);
	}

	@Override
	public boolean doesEmailExist(String email) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		return mongoTemplate.exists(query, Player.class);		
	}
}
