package com.felix.interlogic.game;

import javax.persistence.EntityManager;

import com.felix.interlogic.game.db.dao.impl.GameDao;
import com.felix.interlogic.game.db.dao.impl.UnitDao;
import com.felix.interlogic.game.db.dao.impl.UserDao;
import com.felix.interlogic.game.db.util.HibernateUtil;
import com.felix.interlogic.game.model.Game;
import com.felix.interlogic.game.model.Unit;
import com.felix.interlogic.game.model.User;
import com.felix.interlogic.game.model.UserGame;

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
