package com.felix.interlogic.game.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
	private MessageDigest md;

	private PasswordUtil() {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	};

	private static PasswordUtil instance;

	public static PasswordUtil instance() {
		if (instance == null)
			instance = new PasswordUtil();
		return instance;
	}

	public String encode(String password) {
		StringBuffer encoded = new StringBuffer();
		md.update(password.getBytes());
		byte byteData[] = md.digest();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				encoded.append('0');
			encoded.append(hex);
		}
		return encoded.toString();
	}

	public boolean passwordEquals(String password, String encodedPassword) {
		return encode(password).equals(encodedPassword);
	}
}
