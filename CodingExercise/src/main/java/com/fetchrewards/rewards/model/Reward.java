package com.fetchrewards.rewards.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Reward {
	@Id @GeneratedValue  
	private long Id;
	private String payer;
	private long points;
	private Date timestamp;
	public Reward(){
		
	}
	public Reward(String payer, long points, Date timestamp) {
		super();
		this.payer = payer;
		this.points = points;
		this.timestamp = timestamp;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Id, payer, points, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reward other = (Reward) obj;
		return Id == other.Id && Objects.equals(payer, other.payer) && points == other.points
				&& Objects.equals(timestamp, other.timestamp);
	}

	@Override
	public String toString() {
		return "Rewards [Id=" + Id + ", payer=" + payer + ", points=" + points + ", timestamp=" + timestamp + "]";
	}
	
	
}