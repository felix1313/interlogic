package com.felix.game.map.model;

import java.io.Serializable;
import java.util.Random;

public class Location implements Serializable {

	private static final long serialVersionUID = 6287897665472043709L;

	private double x;
	private double y;

	public Location() {

	}

	public Location(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Location(Location other) {
		this.x = other.x;
		this.y = other.y;
	}

	public Location shift(int brushCoef) {
		int indX = (int) (x - ((int) (x + 0.0001)) % brushCoef);
		int indY = (int) (y - ((int) (y + 0.0001)) % brushCoef);
		return new Location(indX, indY);
	}

	public double vectMult(Location b) {
		return x * b.y - y * b.x;
	}

	public Location vectorTo(Location b) {
		return new Location(b.x - x, b.y - y);
	}

	public double[] lineEq(Location otherPoint) {
		double[] res = new double[3];
		res[0] = otherPoint.y - y;
		res[1] = x - otherPoint.x;
		res[2] = y * (otherPoint.x - x) + x * (y - otherPoint.y);
		return res;
	}

	public static Location lineIntersect(double a1, double b1, double c1,
			double a2, double b2, double c2) {
		double y = (a1 * c2 - a2 * c1 + 0.0) / (b1 * a2 - a1 * b2 + 0.0);
		double x = (-c2 - b2 * y) / a2;
		return new Location(x, y);
	}

	public boolean between(Location a, Location b) {
		return this.vectorTo(b).vectMult(this.vectorTo(a)) == 0
				&& Math.abs(this.dist(a) + this.dist(b) - a.dist(b)) < 1e-5;
	}

	public static Location intersect(Location a, Location b, Location c,
			Location d) {
		boolean intersects = true;
		if (a.equals(b) && c.equals(d))
			return a.equals(d) ? a : null;
		if (a.equals(b) && !c.equals(d)) {
			Location t = a;
			a = c;
			c = t;
			t = b;
			b = d;
			d = t;
		}
		Location ac = a.vectorTo(c);
		Location ab = a.vectorTo(b);
		Location ad = a.vectorTo(d);
		Location cd = c.vectorTo(d);
		if (ab.vectMult(ac) * ab.vectMult(ad) > 0)
			intersects = false;
		if (cd.vectMult(c.vectorTo(a)) * cd.vectMult(c.vectorTo(b)) > 0)
			intersects = false;
		if (!intersects)
			return null;
		// parallel
		if (ab.vectMult(cd) == 0)
			return c.between(a, b) ? c : null;

		double[] line1 = a.lineEq(b);
		double[] line2 = c.lineEq(d);

		return lineIntersect(line1[0], line1[1], line1[2], line2[0], line2[1],
				line2[2]);
	}

	public static Location subtract(Location a, Location b, double dist) {
		double oldDist = a.dist(b);

		return a.vectorFrom(a.vectorTo(b).zoomOut(oldDist)
				.zoomIn(oldDist - dist));
	}

	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + "]";
	}

	public Location zoomOut(double coef) {
		return new Location(x / coef, y / coef);
	}

	public Location zoomIn(double coef) {
		return new Location(x * coef, y * coef);
	}

	public double dist(Location other) {
		return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y)
				* (y - other.y));
	}

	public double dist2(Location other) {
		return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y);
	}

	public double getX() {
		return x;
	}

	public int getIntX() {
		return (int) Math.round(x);
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public int getIntY() {
		return (int) Math.round(y);
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * point received by vector starting from this point
	 * 
	 * @return
	 */
	private Location vectorFrom(Location vector) {
		Location b = new Location(x + vector.x, y + vector.y);
		return b;
	}

	public Location add(Location a) {
		return new Location(x + a.x, y + a.y);
	}

	public static Location getRandomLocation(Map map) {
		Random rnd = new Random();
		return new Location(rnd.nextInt(map.getHeight()), rnd.nextInt(map
				.getWidth()));
	}

	public static Location getRandomAccessibleLocation(Map map) {
		Random rnd = new Random();
		Location loc = new Location(rnd.nextInt(map.getHeight()),
				rnd.nextInt(map.getWidth()));
		while (!map.canGo(loc.getIntX(), loc.getIntY())) {
			loc.setX(rnd.nextInt(map.getHeight()));
			loc.setY(rnd.nextInt(map.getWidth()));
		}
		return loc;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		final double eps = 1e-8;
		if (Math.abs(x - other.x) > eps)
			return false;
		if (Math.abs(y - other.y) > eps)
			return false;
		return true;
	}

}
