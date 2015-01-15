package com.felix.game.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordUtilTest {

	@Test
	public void testPasswordEquals() {
		String test = "14686877553";
		String code = PasswordUtil.instance().encode(test);
		assertTrue(PasswordUtil.instance().passwordEquals(test, code));
	}

}
