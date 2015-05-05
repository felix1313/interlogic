package com.felix.game.RTutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.felix.game.dto.UnitPathDTO;
import com.felix.game.map.model.Location;
import com.felix.game.server.GameRoom;

public class UnitMoveModel {
	private final Logger log = Logger.getLogger(getClass());
	private final long timeEPS = 15;
	private int unitId;
	private ArrayList<TimeLocationPair> path;
	private boolean crashes = false;
	private long crashTime = Long.MAX_VALUE;
	private Location crashLocation;
	private Location correctedCrashLocation;
	private Location rejectedLocation;
	private Map<Integer, UnitMoveModel> unitsMap;
	private GameRoom gameRoom;
	private Map<UnitMoveModel, Location> crashSet = new HashMap<UnitMoveModel, Location>();
	private static Set<UnitMoveModel> currentChecking = new HashSet<UnitMoveModel>();

	public TimeLocationPair get(int index) {
		return path.get(index);
	}

	public int pathSize() {
		return path.size();
	}

	public void updatePath(ArrayList<TimeLocationPair> newPath) {
		this.path = null;
		this.crashes = false;
		this.rejectedLocation = null;
		this.crashTime = Long.MAX_VALUE;
		this.crashLocation = null;
		if (crashSet != null) {
			Set<UnitMoveModel> crashSetCopy = crashSet.keySet();
			this.crashSet = null;
			for (UnitMoveModel crashed : crashSetCopy) {
				crashed.rejectCrash(this);
			}
		}
		this.path = newPath;
		log.trace("update path " + path);
		this.checkCrash();
	}

	public void rejectCrash(UnitMoveModel crashModel) {
		this.crashSet.remove(crashModel);
		if (this.crashSet == null || this.crashSet.isEmpty()) {
			this.crashTime = Long.MAX_VALUE;
			this.crashLocation = null;
			this.crashes = false;
			this.checkCrash();
		}
	}

	public UnitMoveModel(GameRoom gameRoom, int id, Location location) {
		log.trace("creating new user model id=" + id + " " + location);
		this.unitId = id;
		this.gameRoom = gameRoom;
		this.unitsMap = gameRoom.getUnitMoveModels();
		this.path = new ArrayList<TimeLocationPair>();
		path.add(new TimeLocationPair(System.currentTimeMillis(), location));
	}

	public void addMove(UnitPathDTO unitPath, double speed) {
		List<Location> inputPath = unitPath.getPath();
		log.trace("move unit id=" + unitId + " path=" + inputPath);
		long curTime = unitPath.getTime();
		ArrayList<TimeLocationPair> timedPath = new ArrayList<TimeLocationPair>();
		Location prev = inputPath.get(0);
		for (Location loc : inputPath) {
			double dist = prev.dist(loc);
			curTime += dist / speed;
			timedPath.add(new TimeLocationPair(curTime, loc));
			prev = loc;
		}
		this.updatePath(timedPath);
	}

	public void checkSegmentCrash(TimeLocationPair left, TimeLocationPair right) {
		log.trace("checking segment [" + left + " ; " + right + "]");
		// rejected locations of units I crash with

		for (Entry<Integer, UnitMoveModel> es : unitsMap.entrySet()) {
			UnitMoveModel curModel = es.getValue();
			if (curModel.getPath() == null || curModel == this)
				continue;
			TimeLocationPair otherRight, otherLeft;

			for (int j = 1; j < curModel.pathSize(); j++) {

				otherRight = curModel.get(j);
				otherLeft = curModel.get(j - 1);

				// detect segment intersection
				Location intersect = Location.intersect(left.getLocation(),
						right.getLocation(), otherLeft.getLocation(),
						otherRight.getLocation());
				if (intersect == null)
					continue;
				long t1Intersect = timeAt(left, right, intersect);
				long t2Intersect = timeAt(otherLeft, otherRight, intersect);
				if (Math.abs(t1Intersect - t2Intersect) < timeEPS) { // crash!
					t1Intersect = (t1Intersect + t2Intersect) / 2;
					log.info("intersect detected at " + intersect + "  with ["
							+ otherLeft + " ; " + otherRight + "] ");
					// if crash is respected already check if new crash is
					// faster
					if (t1Intersect - timeEPS > curModel.getCrashTime()) {
						break;
					}

					if (t1Intersect < this.crashTime) {
						this.crashes = true;
						this.crashLocation = intersect;
						correctedCrashLocation = Location.subtract(
								left.getLocation(), intersect, 1);
						this.rejectedLocation = right.getLocation();
						long oldCrashTime = this.crashTime;
						this.crashTime = t1Intersect;
						if (Math.abs(crashTime - oldCrashTime) > timeEPS) {
							this.crashSet.clear();
						}
						this.crashSet.put(curModel, otherRight.getLocation());

						log.trace("crash detected at" + crashLocation);
						log.trace("crash time :" + t1Intersect);
					}
				}

				if (otherRight.equals(curModel.getRejectedLocation()))
					break;
			}
			// check last point crash
			this.checkLastPoint(curModel, left, right);
		}
	}

	/**
	 * @param a
	 * @param b
	 * @param p
	 *            must be on [a ; b ] segment
	 * @return
	 */
	private static long timeAt(TimeLocationPair a, TimeLocationPair b,
			Location p) {
		return a.getTime()
				+ Math.round((b.getTime() - a.getTime())
						/ a.getLocation().dist(b.getLocation())
						* a.getLocation().dist(p));
	}

	private TimeLocationPair getLastPoint() {
		TimeLocationPair lastLocation;
		if (this.crashes) {
			lastLocation = new TimeLocationPair(this.crashTime, crashLocation);
		} else {
			lastLocation = this.get(path.size() - 1);
		}
		return lastLocation;
	}

	/**
	 * 
	 * @param left
	 *            segment left
	 * @param right
	 *            segment right
	 * @return time Of Crash at last point of model or -1 (no crash)
	 */
	private void checkLastPoint(UnitMoveModel model, TimeLocationPair left,
			TimeLocationPair right) {

		TimeLocationPair last = model.getLastPoint();
		log.trace("checking last point " + last + " witth " + left + " "
				+ right);
		Location intersect = Location.intersect(last.getLocation(),
				last.getLocation(), left.getLocation(), right.getLocation());
		if (intersect == null)
			return;
		log.trace("path intersect detected " + intersect);
		long timeAtIntersect = timeAt(left, right, intersect);
		if (timeAtIntersect + timeEPS > last.getTime()
				&& timeAtIntersect + timeEPS < this.crashTime) { // crashes
			log.trace("crash at last point ");
			this.crashes = true;
			this.crashLocation = intersect;
			this.rejectedLocation = right.getLocation();
			long oldCrashTime = this.crashTime;
			this.crashTime = timeAtIntersect;
			this.correctedCrashLocation = Location.subtract(left.getLocation(),
					crashLocation, 1);
			if (Math.abs(crashTime - oldCrashTime) > timeEPS) {
				if (this.crashSet == null)
					this.crashSet = new HashMap<UnitMoveModel, Location>();
				this.crashSet.clear();
			}
			this.crashSet.put(model, null);
		} else {
			log.trace("no crash : timelast =" + last.getTime()
					+ " timeIntersect=" + timeAtIntersect);
		}
	}

	public void checkCrash() {
		if (UnitMoveModel.currentChecking.contains(this))
			return;
		currentChecking.add(this);
		log.trace("check crash");
		TimeLocationPair left, right;
		for (int i = 1; i < pathSize() && !this.crashes; i++) {
			left = get(i - 1);
			right = get(i);
			checkSegmentCrash(left, right);
		}

		if (this.crashes) {
			gameRoom.reportCrash(unitId, rejectedLocation,
					correctedCrashLocation);
			log.trace("sending crash report at" + crashLocation);

			if (this.crashSet != null)
				this.crashSet.forEach((model, location) -> {
					model.updateCrash(this, this.crashTime, location,
							crashLocation, rejectedLocation);
					if (location != null) {
						gameRoom.reportCrash(model.unitId, location,
								crashLocation);
					}
				});
		} else
			gameRoom.reportCrash(unitId, null, null);

		currentChecking.remove(this);
	}

	private void updateCrash(UnitMoveModel crashModel, long newCrashTime,
			Location crashLocation, Location rejectedLocation,
			Location otherRejectedLocation) {
		long oldCrashTime = this.crashTime;
		this.crashTime = newCrashTime;
		if (rejectedLocation != null) {
			this.rejectedLocation = rejectedLocation;
			this.crashLocation = crashLocation;
			this.crashes = true;
		}
		if (Math.abs(newCrashTime - oldCrashTime) > timeEPS) {// delete previous
																// // crashset
			Map<UnitMoveModel, Location> crashSetCopy = this.crashSet;
			this.crashSet = null;
			if (crashSetCopy != null) {
				for (UnitMoveModel model : crashSetCopy.keySet()) {
					model.rejectCrash(this);
				}
			}
			this.crashSet = crashSetCopy;
		}
		if (this.crashSet == null) {
			// log.warn("unexpected null crashset ");
			crashSet = new HashMap<UnitMoveModel, Location>();
		}
		crashSet.put(crashModel, otherRejectedLocation);
	}

	public long getCrashTime() {
		return crashTime;
	}

	public void setCrashTime(long crashTime) {
		this.crashTime = crashTime;
	}

	public Location getCrashLocation() {
		return crashLocation;
	}

	public void setCrashLocation(Location crashLocation) {
		this.crashLocation = crashLocation;
	}

	public Location getRejectedLocation() {
		return rejectedLocation;
	}

	public void setRejectedLocation(Location rejectedLocation) {
		this.rejectedLocation = rejectedLocation;
	}

	public ArrayList<TimeLocationPair> getPath() {
		return path;
	}

	public void setPath(ArrayList<TimeLocationPair> path) {
		this.path = path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + unitId;
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
		UnitMoveModel other = (UnitMoveModel) obj;
		if (unitId != other.unitId)
			return false;
		return true;
	}
}
