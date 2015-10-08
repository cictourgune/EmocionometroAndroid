package org.tourgune.emocionometro.bean;

public class EgistourBean{
	private int id;
	private float valueLat;
	private float valueLng;
	private int numSatellites;
	
	public EgistourBean(){
		
	}
	public EgistourBean(int id, float valueLat, float valueLng,int numSatellites){
		this.id=id;
		this.valueLat=valueLat;
		this.valueLng=valueLng;
		this.numSatellites=numSatellites;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getValueLat() {
		return valueLat;
	}
	public void setValueLat(float valueLat) {
		this.valueLat = valueLat;
	}
	public float getValueLng() {
		return valueLng;
	}
	public void setValueLng(float valueLng) {
		this.valueLng = valueLng;
	}
	public int getNumSatellites() {
		return numSatellites;
	}
	public void setNumSatellites(int numSatellites) {
		this.numSatellites = numSatellites;
	}
	
}