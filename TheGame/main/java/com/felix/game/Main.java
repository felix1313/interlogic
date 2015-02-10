package com.felix.game;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.felix.game.db.util.HibernateUtil;
import com.felix.game.server.Server;

public class Main {

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		Logger.getLogger("main").info("starting game server...");
		new Server().start();
		HibernateUtil.exit();
	}
}
