package com.play.linesOfAction.controller.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.play.linesOfAction.model.Player;

/**
 * PlayerRepository
 */
@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

	
}
