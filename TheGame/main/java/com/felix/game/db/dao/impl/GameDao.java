package com.felix.game.db.dao.impl;

import com.felix.game.model.Game;

public class GameDao extends GenericDaoJpa<Game, Integer> {

	private GameDao() {
	};

	private static class Holder {
		static final GameDao INSTANCE = new GameDao();
	}

	public static GameDao instance() {
		return Holder.INSTANCE;
	}

	@Override
	protected void fetchCollections(Game entity) {
		entity.getUserGames().size();
	}
}
