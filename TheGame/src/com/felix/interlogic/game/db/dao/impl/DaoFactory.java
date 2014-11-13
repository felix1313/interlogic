package com.felix.interlogic.game.db.dao.impl;

public class DaoFactory {
	private DaoFactory() {
	}

	private static DaoFactory instance;

	public static synchronized DaoFactory getInstance() {
		if (instance == null) {
			instance = new DaoFactory();
		}
		return instance;
	}

	private UserDao userDao;
	private GameDao gameDao;

	public UserDao getUserDao() {
		if (userDao == null)
			userDao = new UserDao();
		return userDao;
	}

	public GameDao getGameDao() {
		if (gameDao == null)
			gameDao = new GameDao();
		return gameDao;
	}
}
