package com.felix.game.db.dao.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.felix.game.exception.DaoException;
import com.felix.game.model.User;

public class UserDaoTest {

	@Test(expected = DaoException.class)
	public void testReadByLogin() throws DaoException {
		UserDao.instance().readByLogin("-----kk");
	}

	@Test
	public void testReadAll() {
		UserDao.instance().readAll();
	}

	@Test(expected=DaoException.class)
	public void testCreate() throws DaoException {
		String name=String.valueOf(Math.random()).substring(0,10);
		UserDao.instance().create(new User(name,"pass"));
		UserDao.instance().create(new User(name,"pass"));
	}

	/*
	 * @Test public void testCreate() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testReadPK() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testUpdate() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testDelete() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testReadAll() { fail("Not yet implemented"); }
	 */
}
