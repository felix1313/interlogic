package com.felix.game;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.felix.game.db.dao.impl.UserDao;
import com.felix.game.db.util.HibernateUtil;
import com.felix.game.exception.DaoException;

public class Main {

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		try {
			System.out.println(UserDao.instance().readByLogin("Vanya").getLogin());
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HibernateUtil.exit();
	}
}
