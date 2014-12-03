package com.felix.interlogic.game;

import com.felix.interlogic.game.db.dao.impl.GameDao;
import com.felix.interlogic.game.db.dao.impl.UserDao;
import com.felix.interlogic.game.db.exception.DaoException;
import com.felix.interlogic.game.db.util.HibernateUtil;
import com.felix.interlogic.game.model.Game;
import com.felix.interlogic.game.model.User;

public class Main {

	public static void main(String[] args) {

		String ss = "";

		for (int i = 0; i < 1; i++)
			ss += new Double(Math.random()).toString();
		User v = new User(ss, "pp");
		try {

			Game g = new Game();
			v.setGame(g);
			GameDao.instance().create(g);
			UserDao.instance().create(v);
			g = GameDao.instance().read(g.getGameId(), true);
			System.out.println(UserDao.instance().read(v.getId(), true));
			System.out.println(g);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtil.getEntityManagerFactory().close();
		}
		/*
		 * User u = new User("poi", "pp"); Game g = new Game(); v.setGame(g);
		 * u.setGame(g);
		 * 
		 * try { DaoFactory.getInstance().getGameDao().create(g); } catch
		 * (DaoException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * try { DaoFactory.getInstance().getUserDao().create(u); } catch
		 * (DaoException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } try {
		 * DaoFactory.getInstance().getUserDao().create(v); } catch
		 * (DaoException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * System.out.println(g.getPlayers());
		 * DaoFactory.getInstance().getGameDao().refresh(g);
		 * 
		 * System.out.println(DaoFactory.getInstance().getGameDao()
		 * .read(g.getGameId()));
		 */
	}
}
