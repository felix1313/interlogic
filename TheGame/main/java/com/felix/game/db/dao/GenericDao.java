package com.felix.game.db.dao;

import java.io.Serializable;
import java.util.List;

import com.felix.game.exception.DaoException;

public interface GenericDao<T, PK extends Serializable> {
	T create(T t) throws DaoException;

	/**
	 * 
	 * @param id
	 *            Entity PK
	 * @param fetch
	 *            - true if lazy-load collections need to be loaded
	 * @return
	 */
	T read(PK id, boolean fetch);

	List<T> readAll();

	T update(T t) throws DaoException;

	void delete(PK key) throws DaoException;

	List<T> readByCondition(String condition) throws DaoException;
}
