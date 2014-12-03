package com.felix.interlogic.game.db.dao.impl;

import com.felix.interlogic.game.model.Game;

public class GameDao extends GenericDaoJpa<Game, Long> {

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
		entity.getPlayers().size();
	}

}
