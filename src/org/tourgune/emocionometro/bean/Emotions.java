package org.tourgune.emocionometro.bean;

import android.location.Location;

public class Emotions {

	
	public Emotions(int user, int pleasure, int arousal, int dominance,
			Double latitude, Double longitude, String provider, String date) {
		super();
		this.user = user;
		this.pleasure = pleasure;
		this.arousal = arousal;
		this.dominance = dominance;
		this.latitude = latitude;
		this.longitude = longitude;
		this.provider = provider;
		this.date = date;
	}
	
	private int user;
	private int pleasure;
	private int arousal;
	private int dominance;
	private Double latitude;
	private Double longitude;
	private String provider;
	private String date;
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public int getPleasure() {
		return pleasure;
	}
	public void setPleasure(int pleasure) {
		this.pleasure = pleasure;
	}
	public int getArousal() {
		return arousal;
	}
	public void setArousal(int arousal) {
		this.arousal = arousal;
	}
	public int getDominance() {
		return dominance;
	}
	public void setDominance(int dominance) {
		this.dominance = dominance;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
	
}
