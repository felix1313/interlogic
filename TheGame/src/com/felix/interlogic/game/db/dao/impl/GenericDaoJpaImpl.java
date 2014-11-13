package com.felix.interlogic.game.db.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import com.felix.interlogic.game.db.dao.GenericDao;
import com.felix.interlogic.game.db.exception.DaoException;
import com.felix.interlogic.game.db.util.HibernateUtil;

public class GenericDaoJpaImpl<T, PK extends Serializable> implements
		GenericDao<T, PK> {

	protected Class<T> type;

	protected EntityManager em;

	@SuppressWarnings("unchecked")
	public GenericDaoJpaImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		this.type = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
	}

	@Override
	public T create(T t) throws DaoException {
		try {
			em.getTransaction().begin();
			em.persist(t);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new DaoException(e.getMessage());
		}
		return t;
	}

	@Override
	public T read(PK id) {
		return this.em.find(type, id);
	}

	@Override
	public T update(T t) throws DaoException {
		T res = null;
		try {
			em.getTransaction().begin();
			res = this.em.merge(t);

			em.flush();
			em.getTransaction().commit();

		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new DaoException(e.getMessage());
		}
		return res;
	}

	@Override
	public void delete(PK key) throws DaoException {
		T obj = this.read(key);
		try {
			em.getTransaction().begin();
			em.remove(obj);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new DaoException(e.getMessage());
		}

	}

	@Override
	public List<T> readAll() {

		TypedQuery<T> query = em.createQuery("SELECT c FROM ?1 c", type);
		query.setParameter(1, type.getName());
		return query.getResultList();
	}

	@Override
	public List<T> readByCondition(String condition) throws DaoException {
		try {
			TypedQuery<T> query = em.createQuery("SELECT c FROM ?1 "
					+ " WHERE " + condition, type);
			query.setParameter(1, type.getName());
			return query.getResultList();
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
	}

	public void refresh(T t) {
		em.refresh(t);
	}

}