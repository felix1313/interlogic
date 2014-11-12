package com.felix.interlogic.game.db.util;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	private static StandardServiceRegistry serviceRegistry = null;

	private static void createSessionFactory() {
		Configuration configuration = new Configuration();
		configuration.setSessionFactoryObserver(new SessionFactoryObserver() {
			@Override
			public void sessionFactoryCreated(SessionFactory factory) {
			}

			@Override
			public void sessionFactoryClosed(SessionFactory factory) {
				StandardServiceRegistryBuilder.destroy(serviceRegistry);
			}
		});
		configuration.configure();
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
				configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null)
			createSessionFactory();
		return sessionFactory;
	}
}
