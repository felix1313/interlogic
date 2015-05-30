package com.felix.game.map.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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

	public boolean canGo(int x, int y) {
		return x >= 0 && y >= 0 && y < width && x < height
				&& getMap()[x][y] == GRASS;
	}

	private class Path implements Comparable<Path> {
		private Location location;
		private Double dist;
		private Path parent;
		private Double g;

		public Path(Location location, Double h, Double g) {
			this.location = location;
			this.dist = h + g;
			this.g = g;
		}

		public Path(Location location, Double dist) {
			this.location = location;
			this.dist = dist;
		}

		@Override
		public int compareTo(Path o) {
			return Double.compare(dist, o.dist);
		}

		public Double getDist() {
			return dist;
		}

		public void setDist(Double dist) {
			this.dist = dist;
		}

		public int getX() {
			return location.getIntX();
		}

		public int getY() {
			return location.getIntY();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((dist == null) ? 0 : dist.hashCode());
			result = prime * result
					+ ((location == null) ? 0 : location.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Path other = (Path) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (dist == null) {
				if (other.dist != null)
					return false;
			} else if (Math.abs(dist - other.dist) > 1e-4)
				return false;
			if (location == null) {
				if (other.location != null)
					return false;
			} else if (!location.equals(other.location))
				return false;
			return true;
		}

		private Map getOuterType() {
			return Map.this;
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public Path getParent() {
			return parent;
		}

		public void setParent(Path parent) {
			this.parent = parent;
		}

		public Double getG() {
			return g;
		}

		public void setG(Double g) {
			this.g = g;
		}

	}

	private double heuristicDist(Location a, Location b) {
		double dx = Math.abs(a.getX() - b.getX());
		double dy = Math.abs(a.getY() - b.getY());
		return Math.min(dx, dy) * Math.sqrt(2) + Math.max(dx, dy)
				- Math.min(dx, dy);
	}

	private List<Location> trimPath(List<Location> path) {
	//	if(path.size()<3)return new ArrayList<Location>(path);
		List<Location> res = new ArrayList<Location>();
		res.add(path.get(0));
		for (int i = 1; i < path.size() - 1; i++) {
			if (Math.abs(path.get(i).dist(path.get(i - 1))
					- path.get(i + 1).dist(path.get(i))) > 1e-5) {
				res.add(path.get(i));
			}
		}
		if(path.size()>1)
		res.add(path.get(path.size() - 1));
		return res;
	}

	/**
	 * A* algorithm to find shortest path
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Location> getPath(Location from, Location to) {
		LinkedList<Location> path = new LinkedList<Location>();
		PriorityQueue<Path> q = new PriorityQueue<Path>();
		// HashSet<Path> used = new HashSet<Path>();
		Double dist[][] = new Double[height][width];
		dist[from.getIntX()][from.getIntY()] = heuristicDist(from, to);
		q.add(new Path(from, 0.0, dist[from.getIntX()][from.getIntY()]));

		while (q.size() > 0) {
			Path cur = q.poll();
			if (cur.getLocation().equals(to)) {
				for (; cur != null; cur = cur.getParent())
					path.addFirst(cur.location);
				break;
			}
			for (int i = cur.getX() - 1; i <= cur.getX() + 1; i++)
				for (int j = cur.getY() - 1; j <= cur.getY() + 1; j++) {
					if (canGo(i, j)) {
						double dst = 1;
						if (i != cur.getX() && j != cur.getY())
							dst = Math.sqrt(2.0);
						Location loc = new Location(i, j);
						double cost = cur.getG() + dst + heuristicDist(loc, to);
						if (dist[i][j] == null || cost < dist[i][j]) {
							Path ld = new Path(new Location(i, j), dist[i][j]);
							q.remove(ld);
							dist[i][j] = cost;
							ld.setDist(cost);
							ld.setG(cur.getG() + dst);
							ld.setParent(cur);
							q.add(ld);
						}
					}
				}
		}
		return trimPath(path);
	}
}
