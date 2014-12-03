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
		
		
	}
}
