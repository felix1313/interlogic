package com.felix.interlogic.game;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.felix.interlogic.game.db.dao.impl.DaoFactory;
import com.felix.interlogic.game.db.exception.DaoException;
import com.felix.interlogic.game.db.util.HibernateUtil;
import com.felix.interlogic.game.model.Game;
import com.felix.interlogic.game.model.User;

public class Main {

	public static void main(String[] args) {

		User v = new User("Vanya", "pp");
		User u = new User("poi", "pp");
		Game g = new Game();
		v.setGame(g);
		u.setGame(g);

		try {
			DaoFactory.getInstance().getGameDao().create(g);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DaoFactory.getInstance().getUserDao().create(u);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DaoFactory.getInstance().getUserDao().create(v);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(g.getPlayers());
		DaoFactory.getInstance().getGameDao().refresh(g);

		System.out.println(DaoFactory.getInstance().getGameDao()
				.read(g.getGameId()));

	}
}
