package com.felix.game.AI;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.felix.game.dto.UserLocationDTO;
import com.felix.game.map.model.Location;
import com.felix.game.model.MapModel;
import com.felix.game.model.UnitModel;

public class GameBot implements Runnable {

	private static int botIdCounter = 1;
	public static final ExecutorService gameBotExecutor = Executors
			.newCachedThreadPool();
	private MapModel map;
	// must be negative
	private int botId;
	private UnitModel botModel;
	private static long moveDelay = 2000;
	private static long shootDelay = 700;

	public GameBot(MapModel map) {
		Location initLocation = Location.getRandomAccessibleLocation(map
				.getMap());
		this.map = map;
		this.botId = -(botIdCounter++);
		this.botModel = new UnitModel(new UserLocationDTO(botId, initLocation),
				map);
		map.markUnit(botModel);
	}

	public GameBot(MapModel map, Location initLocation) {
		this.map = map;
		this.botId = -(botIdCounter++);
		this.botModel = new UnitModel(new UserLocationDTO(botId, initLocation),
				map);
		map.markUnit(botModel);
	}

	@Override
	public void run() {
		Random rnd = new Random();
		long sleep, curTime, beforeTime;
		long lastmove = System.currentTimeMillis();
		while (true) {
			beforeTime = System.currentTimeMillis();
			sleep = shootDelay + rnd.nextInt(500);
			Location shootTarget = Location.getRandomLocation(map.getMap())
					.zoomIn(map.getBrushcoef());
			botModel.shoot(shootTarget);
			curTime = System.currentTimeMillis();
			if (curTime - lastmove > moveDelay + rnd.nextInt(2000))// move
			{
				lastmove = curTime;
				Location moveTarget = Location.getRandomAccessibleLocation(
						map.getMap()).zoomIn(map.getBrushcoef());
				botModel.move(moveTarget);
			}
			sleep -= System.currentTimeMillis() - beforeTime;
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public MapModel getMap() {
		return map;
	}

	public void setMap(MapModel map) {
		this.map = map;
	}

	public int getBotId() {
		return botId;
	}

	public void setBotId(int botId) {
		this.botId = botId;
	}

	public UnitModel getBotModel() {
		return botModel;
	}

	public void setBotModel(UnitModel botModel) {
		this.botModel = botModel;
	}

	public static void updIdCounter(int id) {
		botIdCounter = Math.max(botIdCounter, Math.abs(id) + 1);
	}

	@Override
	public String toString() {
		return "GameBot [map=" + map + ", botId=" + botId + ", botModel="
				+ botModel + "]";
	}

}
