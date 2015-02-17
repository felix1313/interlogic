package com.felix.game.map.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class MapTest {

	@Test
	public void testGetPath() {
		Map map = new Map();
		List<Location> path = map.getPath(new Location(0, 0),
				new Location(2, 10));
		System.out.println(path);
	}

}
