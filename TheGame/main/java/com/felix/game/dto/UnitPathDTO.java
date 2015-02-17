package com.felix.game.dto;

import java.io.Serializable;
import java.util.List;

import com.felix.game.map.model.Location;

public class UnitPathDTO implements Serializable {

	private static final long serialVersionUID = -3446028900832264546L;
	private int userId;
	private List<Location> path;

	public UnitPathDTO(int userId, List<Location> path) {
		this.userId = userId;
		this.path = path;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<Location> getPath() {
		return path;
	}

	public void setPath(List<Location> path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "UnitPathDTO [userId=" + userId + ", path=" + path + "]";
	}

}
