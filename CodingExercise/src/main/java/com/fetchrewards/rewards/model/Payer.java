package com.fetchrewards.rewards.model;

import java.util.Objects;

public class Payer {
	public Payer(){
		
	}
	
	private String payer;
	private long points;
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
	@Override
	public int hashCode() {
		return Objects.hash(payer, points);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payer other = (Payer) obj;
		return Objects.equals(payer, other.payer) && points == other.points;
	}

}
