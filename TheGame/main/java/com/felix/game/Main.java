package com.felix.game;

import javax.persistence.EntityManager;

import com.felix.game.db.dao.impl.GameDao;
import com.felix.game.db.dao.impl.UnitDao;
import com.felix.game.db.dao.impl.UserDao;
import com.felix.game.db.util.HibernateUtil;
import com.felix.game.model.Game;
import com.felix.game.model.Unit;
import com.felix.game.model.User;
import com.felix.game.model.UserGame;

public class Main {

	public static void main(String[] args) {

		try {
			
			System.out.println(GameDao.instance().readAll());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback");
		} finally {
			HibernateUtil.getEntityManagerFactory().close();
		}
	}
}
