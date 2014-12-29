package com.felix.interlogic.game.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "unit")
public class Unit {
	private Integer unitId;
	private String name;
	private Integer power;
	private Integer speed;
	private Integer hp;
	private Integer coefPower;
	private Integer coefSpeed;
	private Integer coefHp;
	private Set<UserGame> userGames;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "unit_id", nullable = false, unique = true)
	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "power")
	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	@Column(name = "speed")
	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	@Column(name = "health_points")
	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

	@Column(name = "coef_power")
	public Integer getCoefPower() {
		return coefPower;
	}

	public void setCoefPower(Integer coefPower) {
		this.coefPower = coefPower;
	}

	@Column(name = "coef_speed")
	public Integer getCoefSpeed() {
		return coefSpeed;
	}

	public void setCoefSpeed(Integer coefSpeed) {
		this.coefSpeed = coefSpeed;
	}

	@Column(name = "coef_hp")
	public Integer getCoefHp() {
		return coefHp;
	}

	public void setCoefHp(Integer coefHp) {
		this.coefHp = coefHp;
	}

	@OneToMany(fetch=FetchType.LAZY,mappedBy="unit")
	public Set<UserGame> getUserGames() {
		return userGames;
	}

	public void setUserGames(Set<UserGame> userGames) {
		this.userGames = userGames;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coefHp == null) ? 0 : coefHp.hashCode());
		result = prime * result
				+ ((coefPower == null) ? 0 : coefPower.hashCode());
		result = prime * result
				+ ((coefSpeed == null) ? 0 : coefSpeed.hashCode());
		result = prime * result + ((hp == null) ? 0 : hp.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((power == null) ? 0 : power.hashCode());
		result = prime * result + ((speed == null) ? 0 : speed.hashCode());
		result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
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
		Unit other = (Unit) obj;
		if (coefHp == null) {
			if (other.coefHp != null)
				return false;
		} else if (!coefHp.equals(other.coefHp))
			return false;
		if (coefPower == null) {
			if (other.coefPower != null)
				return false;
		} else if (!coefPower.equals(other.coefPower))
			return false;
		if (coefSpeed == null) {
			if (other.coefSpeed != null)
				return false;
		} else if (!coefSpeed.equals(other.coefSpeed))
			return false;
		if (hp == null) {
			if (other.hp != null)
				return false;
		} else if (!hp.equals(other.hp))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (power == null) {
			if (other.power != null)
				return false;
		} else if (!power.equals(other.power))
			return false;
		if (speed == null) {
			if (other.speed != null)
				return false;
		} else if (!speed.equals(other.speed))
			return false;
		if (unitId == null) {
			if (other.unitId != null)
				return false;
		} else if (!unitId.equals(other.unitId))
			return false;
		return true;
	}


}
