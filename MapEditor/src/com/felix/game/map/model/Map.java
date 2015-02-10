package com.felix.game.map.model;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Map implements Serializable {
	private static final long serialVersionUID = -3964386443558682461L;
	private int height;
	private int width;
	private int[][] map;

	public static int GRASS = 0;
	public static int FOREST = 1;
	public static int WATER = 2;

	public Map() {
		this(128, 128);
	}

	public Map(int height, int width) {
		this.height = height;
		this.width = width;
		map = new int[height][width];
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param type
	 *            - one of Map's static fields
	 */
	public void set(int x, int y, int type) {
		map[x][y] = type;
	}

	public static Color numToColor(int num) {
		Color c = Color.GREEN;
		if (num == 1)
			c = Color.GRAY;
		if (num == 2)
			c = Color.BLUE;
		return c;
	}

	public void drawMap(GraphicsContext gc, int zoom) {

		for (int i = 0; i < getHeight(); i++)
			for (int j = 0; j < getWidth(); j++) {
				int type = getMap()[i][j];
				Color c = Map.numToColor(type);
				gc.setFill(c);
				gc.fillRect(i * zoom, j * zoom, zoom, zoom);
			}
	}
}
