package com.play.linesOfAction.controller.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.play.linesOfAction.model.game.Game;

/**
 * GameRepository
 */
@Repository
public interface GameRepository extends MongoRepository<Game, String> {
	
}
