package com.felix.interlogic.game.db.dao.impl;

import java.beans.Expression;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.felix.interlogic.game.db.dao.UserDao;
import com.felix.interlogic.game.db.model.User;
import com.felix.interlogic.game.db.util.HibernateUtil;

public class UserDaoImpl implements UserDao {

	@Override
	public void deleteUser(User user) throws SQLException {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.delete(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() throws SQLException {
		Session session = null;
		List<User> res = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			res = session.createCriteria(User.class).list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return res;
	}

	@Override
	public User getUserById(int userId) throws SQLException {
		Session session = null;
		User res = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			res = (User) session.load(User.class, userId);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return res;
	}

	@Override
	public User getUserByLogin(String userLogin) throws SQLException {
		Session session = null;
		User res = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			List<User> list = session.createCriteria(User.class)
					.add(Restrictions.eq("login", userLogin)).list();
			if (list != null && list.size() > 0)
				res = list.get(0);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return res;
	}

	@Override
	public void addUser(User user) throws SQLException {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}

	@Override
	public void updateUser(User user) throws SQLException {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.update(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}

}
