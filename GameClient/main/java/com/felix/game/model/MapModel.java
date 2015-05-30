package com.felix.game.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.felix.game.map.model.Location;
import com.felix.game.map.model.Map;

public class MapModel {
	private Map map;
	private MapObject occupiedMap[][];
	private final int brushCoef = 5;
	private GraphicsContext mapGraphics;
	private GraphicsContext unitGraphics;

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void drawMap() {
		map.drawMap(mapGraphics, brushCoef);
	}

	public int getBrushcoef() {
		return brushCoef;
	}

	public GraphicsContext getMapGraphics() {
		return mapGraphics;
	}

	public void setMapGraphics(GraphicsContext mapGraphics) {
		this.mapGraphics = mapGraphics;
		this.occupiedMap = new MapObject[(int) mapGraphics.getCanvas()
				.getHeight() + 1][(int) mapGraphics.getCanvas().getWidth() + 1];
	}

	public GraphicsContext getUnitGraphics() {
		return unitGraphics;
	}

	public void setUnitGraphics(GraphicsContext unitGraphics) {
		this.unitGraphics = unitGraphics;
	}

	public boolean canGo(double x, double y) {
		int indX = (int) (x - ((int) (x + 0.0001)) % brushCoef);
		int indY = (int) (y - ((int) (y + 0.0001)) % brushCoef);
		return map.canGo(indX / brushCoef, indY / brushCoef);
	}

	public boolean canGo(Location target) {
		return canGo(target.getX(), target.getY());
	}

	/**
	 * left bottom corner as argument
	 * 
	 * @param x
	 * @param y
	 */
	public void setOccupied(MapObject o) {
		Location center = o.getCenter();
		occupiedMap[center.getIntX()][center.getIntY()] = o;
	}

	public void clearOccupied(MapObject o) {
		Location center = o.getCenter();
		occupiedMap[center.getIntX()][center.getIntY()] = null;
	}

	public List<MapObject> getIntersectList(MapObject o) {
		Location center = o.getCenter();
		List<MapObject> result = new ArrayList<MapObject>();

		int dist = (int) Math.round(o.getRadius()) + UnitModel.UNIT_SIZE + 10;

		for (int i = Math.max(0, center.getIntX() - dist); i <= Math.min(
				occupiedMap.length, center.getIntX() + dist); i++)
			for (int j = Math.max(0, center.getIntY() - dist); j <= Math.min(
					occupiedMap[i].length, center.getIntY() + dist); j++)
				if (o.intersects(occupiedMap[i][j]))
					result.add(occupiedMap[i][j]);
		return result;
	}

	public void markTarget(UnitModel m) {
		double x = m.getTargetX();
		double y = m.getTargetY();
		unitGraphics.setFill(Color.BLACK);
		unitGraphics.fillOval(x, y, UnitModel.UNIT_SIZE, UnitModel.UNIT_SIZE);
	}

	public void markUnit(UnitModel m) {
		setOccupied(m);
		unitGraphics.setFill(m.getColor());
		unitGraphics.fillOval(m.getUnitX(), m.getUnitY(), UnitModel.UNIT_SIZE,
				UnitModel.UNIT_SIZE);
	}

	public void clearLocation(UnitModel model) {
		clearOccupied(model);
		double x = model.getUnitX();
		double y = model.getUnitY();
		unitGraphics.clearRect(x, y, UnitModel.UNIT_SIZE, UnitModel.UNIT_SIZE);
		repaintNeighbours(model.getLocation(), model.getRadius() * 3);
	}

	public void repaintNeighbours(Location a, double d) {
		int dist = (int) Math.ceil(d);
		for (int i = Math.max(0, a.getIntX() - dist); i <= Math.min(a.getIntX()
				+ dist, occupiedMap.length); i++)
			for (int j = Math.max(0, a.getIntY() - dist); j <= Math.min(
					a.getIntY() + dist, occupiedMap[i].length); j++)
				if (occupiedMap[i][j] != null) {
					MapObject o = occupiedMap[i][j];
					o.paint();
				}
	}

	public void clearTarget(UnitModel model) {
		double x = model.getTargetX();
		double y = model.getTargetY();
		unitGraphics.clearRect(x, y, UnitModel.UNIT_SIZE, UnitModel.UNIT_SIZE);
	}

	public MapObject[][] getOccupiedMap() {
		return occupiedMap;
	}

	public void setOccupiedMap(MapObject[][] occupiedMap) {
		this.occupiedMap = occupiedMap;
	}

}
