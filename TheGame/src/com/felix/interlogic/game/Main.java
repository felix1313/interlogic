package com.felix.interlogic.game;

import org.hibernate.Session;

import com.felix.interlogic.game.db.model.User;
import com.felix.interlogic.game.db.util.HibernateUtil;

public class Main {

	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		User user = new User("vaesyta", "123");
		session.save(user);
		session.getTransaction().rollback();
		session.close();
		session.getSessionFactory().close();
	}

}
