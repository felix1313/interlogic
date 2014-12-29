package com.felix.interlogic.game.db.dao.impl;

import com.felix.interlogic.game.model.Unit;

public class UnitDao extends GenericDaoJpa<Unit, Integer> {

	private UnitDao() {
	}

	private static class Holder {
		static final UnitDao INSTANCE = new UnitDao();
	}

	public static UnitDao instance() {
		return Holder.INSTANCE;
	}

	@Override
	protected void fetchCollections(Unit entity) {

	}

}
