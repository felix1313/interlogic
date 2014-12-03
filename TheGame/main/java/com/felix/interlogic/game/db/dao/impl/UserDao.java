package com.felix.interlogic.game.db.dao.impl;

import com.felix.interlogic.game.model.User;

public class UserDao extends GenericDaoJpa<User, Integer> {
	private UserDao() {
	}

	private static class Holder {
		static final UserDao INSTANCE = new UserDao();
	}

	public static UserDao instance() {
		return Holder.INSTANCE;
	}

	@Override
	protected void fetchCollections(User entity) {
		entity.getUserGames().size();
	}
}
