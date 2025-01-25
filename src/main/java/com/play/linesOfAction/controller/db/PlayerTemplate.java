package com.play.linesOfAction.controller.db;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.play.linesOfAction.model.game.Player;

/**
 * PlayerTemplate
 */
@Repository
public class PlayerTemplate implements CustomPlayerRepository {

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
	public Object getGames(String playerId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(playerId));
		query.fields().include("games");

		Document games = mongoTemplate.findOne(query, Document.class, "Players");

		return games.get("games");
	}
}
