package com.felix.game.RTutil;

import com.felix.game.map.model.Location;

public class TimeLocationPair implements Comparable<TimeLocationPair> {
	private long time;
	private Location location;

	public TimeLocationPair(long time, Location location) {
		this.time = time;
		this.location = location;
	}

	@Override
	public int compareTo(TimeLocationPair o) {
		return Long.compare(time, o.time);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
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
		TimeLocationPair other = (TimeLocationPair) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (time != other.time)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimeLocationPair [time=" + time + ", location=" + location
				+ "]";
	}
}
