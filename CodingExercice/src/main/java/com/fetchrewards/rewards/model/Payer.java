package com.fetchrewards.rewards.model;

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
}