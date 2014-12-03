package com.felix.interlogic.game.db.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	private static StandardServiceRegistry serviceRegistry = null;

	private static void createSessionFactory() {
		Configuration configuration = new Configuration();
		configuration.setSessionFactoryObserver(new SessionFactoryObserver() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
		if (sessionFactory == null) {
			createSessionFactory();
			sessionFactory.openSession();
		}
		return sessionFactory;
	}

	private static EntityManagerFactory entityManagerFactory = null;

	synchronized public static EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null)
			entityManagerFactory = Persistence
					.createEntityManagerFactory("com.felix.interlogic.game");
		return entityManagerFactory;
	}

	synchronized public static EntityManager createEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}
}
