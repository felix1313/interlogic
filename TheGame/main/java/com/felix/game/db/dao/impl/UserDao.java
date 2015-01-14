package com.felix.game.db.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.felix.game.db.util.HibernateUtil;
import com.felix.game.exception.DaoException;
import com.felix.game.model.User;

public class UserDao extends GenericDaoJpa<User, Integer> {
	private UserDao() {
	}

	private static final String READ_BY_LOGIN_QUERY = "SELECT u FROM user u WHERE u.login LIKE :login";

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

	public User readByLogin(String login) throws DaoException {
		EntityManager em = HibernateUtil.createEntityManager();
		User res = null;
		try {
			TypedQuery<User> query = em
					.createQuery(
							"SELECT c FROM com.felix.game.model.User c WHERE login= :login",
							User.class).setParameter("login", login);
			res = query.getSingleResult();

		} catch (NoResultException e) {
			e.printStackTrace();
			throw new DaoException("No user with login " + login);
		} finally {
			em.close();
		}
		return res;
	}
}
