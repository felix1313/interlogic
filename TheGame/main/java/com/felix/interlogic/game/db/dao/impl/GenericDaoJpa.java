package com.felix.interlogic.game.db.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.felix.interlogic.game.db.dao.GenericDao;
import com.felix.interlogic.game.db.util.HibernateUtil;
import com.felix.interlogic.game.exception.DaoException;

public abstract class GenericDaoJpa<T, PK extends Serializable> implements
		GenericDao<T, PK> {

	protected Class<T> type;

	@SuppressWarnings("unchecked")
	public GenericDaoJpa() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		this.type = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}

	@Override
	public T create(T t) throws DaoException {
		EntityManager em = null;
		try {
			em = HibernateUtil.createEntityManager();
			em.getTransaction().begin();
			em.persist(t);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new DaoException(e.getMessage());
		} finally {
			em.close();
		}
		return t;
	}

	@Override
	public T read(PK id, boolean fetch) {
		EntityManager em = HibernateUtil.createEntityManager();
		T res = null;
		try {
			res = em.find(type, id);
			if (fetch == true) {
				fetchCollections(res);
			}
		} catch (Exception e) {
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		return res;
	}

	/**
	 * call .size() from all collections
	 */
	protected abstract void fetchCollections(T entity);

	@Override
	public T update(T t) throws DaoException {
		T res = null;
		EntityManager em = HibernateUtil.createEntityManager();
		try {
			em.getTransaction().begin();
			res = em.merge(t);

			em.getTransaction().commit();

		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new DaoException(e.getMessage());
		} finally {
			em.close();
		}
		return res;
	}

	@Override
	public void delete(PK key) throws DaoException {
		T obj = this.read(key, false);
		EntityManager em = HibernateUtil.createEntityManager();
		try {
			em.getTransaction().begin();
			em.remove(obj);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new DaoException(e.getMessage());
		} finally {
			em.close();
		}

	}

	@Override
	public List<T> readAll() {
		EntityManager em = HibernateUtil.createEntityManager();
		TypedQuery<T> query = em.createQuery("SELECT c FROM ?1 c", type);
		query.setParameter(1, type.getName());
		return query.getResultList();
	}

	@Override
	public List<T> readByCondition(String condition) throws DaoException {
		EntityManager em = HibernateUtil.createEntityManager();
		try {
			TypedQuery<T> query = em.createQuery("SELECT c FROM ?1 "
					+ " WHERE " + condition, type);
			query.setParameter(1, type.getName());
			return query.getResultList();
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
	}

}