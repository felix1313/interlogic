package com.felix.interlogic.game;

import javax.persistence.EntityManager;

import com.felix.interlogic.game.db.dao.impl.GameDao;
import com.felix.interlogic.game.db.dao.impl.UserDao;
import com.felix.interlogic.game.db.exception.DaoException;
import com.felix.interlogic.game.db.util.HibernateUtil;
import com.felix.interlogic.game.model.Game;
import com.felix.interlogic.game.model.Unit;
import com.felix.interlogic.game.model.User;
import com.felix.interlogic.game.model.UserGame;

public class Main {

	public static void main(String[] args) {

		EntityManager em = HibernateUtil.createEntityManager();
		try {
			em.getTransaction().begin();
			String login = String.valueOf(Math.random());
			User user = new User(login, "");
			em.persist(user);
			Game game = new Game();
			Unit unit = new Unit();
			unit.setName("unit");
			em.persist(unit);
			UserGame userGame = new UserGame();
			userGame.setGame(game);
			userGame.setUser(user);
			userGame.setUnit(unit);
			game.getUserGames().add(userGame);
			em.persist(game);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback");
			em.getTransaction().rollback();
		} finally {
			em.close();
			HibernateUtil.getEntityManagerFactory().close();
		}
	}
}
