package com.felix.interlogic.game.db.dao;

import java.io.Serializable;
import java.util.List;

import com.felix.interlogic.game.db.exception.DaoException;

public interface GenericDao<T, PK extends Serializable> {
	T create(T t) throws DaoException;
	
	
	T read(PK id);

	List<T> readAll();

	T update(T t) throws DaoException;

	void delete(PK key) throws DaoException;

	List<T> readByCondition(String condition) throws DaoException;
}
