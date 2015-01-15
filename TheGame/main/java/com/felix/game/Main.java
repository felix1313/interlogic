package com.felix.game;

import org.apache.log4j.PropertyConfigurator;

import com.felix.game.db.util.HibernateUtil;
import com.felix.game.server.Server;

public class Main {

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		new Server().start();
		HibernateUtil.exit();
	}
}
