package com.felix.game.map.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class LocationTest {

	@Test
	public void testIntersect() {
		Location a = new Location(0, 0);
		Location b = new Location(1, 1);
		Location c = new Location(0, 1);
		Location d = new Location(1, 0);
		Location intersect = Location.intersect(a, b, c, d);
		assertEquals(new Location(0.5, 0.5), intersect);
	}

	@Test
	public void testIntersect2() {
		Location a = new Location(0, 0);
		Location b = new Location(1, 1);
		Location c = new Location(5, 1);
		Location d = new Location(6, 0);
		Location intersect = Location.intersect(a, b, c, d);
		assertNull(intersect);
	}

	@Test
	public void testIntersect3() {
		Location a = new Location(0, 0);
		Location b = new Location(1, 1);
		Location c = new Location(1, 1);
		Location d = new Location(2, 0);
		Location intersect = Location.intersect(a, b, c, d);
		assertEquals(b, intersect);
	}

	@Test
	public void testIntersect4() {
		Location a = new Location(0, 0);
		Location b = new Location(1, 1);
		Location c = new Location(1, 1);
		Location d = new Location(2, 2);
		Location intersect = Location.intersect(a, b, c, d);
		assertEquals(b, intersect);
	}

	@Test
	public void testIntersect5() {
		Location a = new Location(0, 0);
		Location b = new Location(1, 1);
		Location c = new Location(0, 1);
		Location d = new Location(1, 2);
		Location intersect = Location.intersect(a, b, c, d);
		assertNull(intersect);
	}

	@Test
	public void testIntersect6() {
		Location a = new Location(1, 1);
		Location b = new Location(1, 1);
		Location c = new Location(0, 0);
		Location d = new Location(2, 2);
		Location intersect = Location.intersect(c, d, a, b);
		assertEquals(a, intersect);
	}

	@Test
	public void testIntersect7() {
		Location a = new Location(81, 63);
		Location b = new Location(63, 81);
		Location c = new Location(47, 74);
		Location d = new Location(47, 74);
		Location intersect = Location.intersect(c, d, a, b);
		assertNull(intersect);
	}
	@Test
	public void testIntersect8() {
		Location a = new Location(55, 68);
		Location c = new Location(55, 72);
		Location d = new Location(55, 75);
		Location intersect = Location.intersect(a, a, c, d);
		assertNull(intersect);
	}
	@Test
	public void testIntersect9() {
		Location a = new Location(47, 63);
		Location c = new Location(47, 74);
		Location d = new Location(47, 68);
		Location intersect = Location.intersect(a, a, c, d);
		assertNull(intersect);
	}
	@Test
	public void testIntersect10() {
		Location a = new Location(47, 63);
		Location d = new Location(49, 63);
		Location intersect = Location.intersect(a, a, a, d);
		assertEquals(a, intersect);
	}
	@Test
	public void testIntersect11() {
		Location a = new Location(47, 61);
		Location b = new Location(47, 74);
		Location c = new Location(47, 66);
		Location intersect = Location.intersect(a, a, b, c);
		assertNull( intersect);
	}
	@Test
	public void testIntersect12() {
		Location a = new Location(48, 65);
		Location b = new Location(44, 65);
		Location c = new Location(47, 65);
		Location intersect = Location.intersect(a, a, b, c);
		assertNull( intersect);
	}
	@Test
	public void testIntersect13() {
		Location a = new Location(45, 41);
		Location b = new Location(94, 41);
		Location c = new Location(79, 41);
		Location intersect = Location.intersect(a, b, c, a);
		assertNotNull( intersect);
	}
	@Test
	public void testSubtract() {
		Location a = new Location(47, 0);
		Location b = new Location(47, 20);
		double dist = 5;
		Location before = Location.subtract(a, b, dist);
		Location res = new Location(47, 15);
		assertEquals(res, before);
	}
	@Test
	public void testSubtract1() {
		Location a = new Location(47, 0);
		Location b = new Location(47, 20);
		double dist = 0.5;
		Location before = Location.subtract(b, a, -dist);
		Location res = new Location(47, -dist);
		assertEquals(res, before);
	}

}
