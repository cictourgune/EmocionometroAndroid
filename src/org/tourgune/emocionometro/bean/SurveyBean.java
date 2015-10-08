package org.tourgune.emocionometro.bean;

public class SurveyBean{
	private int id;
	private int userId;
	private int valueArousal;
	private int valuePleasure;
	private int valueDominance;
	private long time;
	private long reactTime;
	
	public SurveyBean(){
		
	}
	public SurveyBean(int id,int userId,int valueArousal,int valuePleasure,int valueDominance, String text,long time,long reactTime){
		this.id=id;
		this.userId=userId;
		this.valueArousal=valueArousal;
		this.valuePleasure=valuePleasure;
		this.valueDominance=valueDominance;
		this.time=time;
		this.reactTime=reactTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getValueArousal() {
		return valueArousal;
	}
	public void setValueArousal(int valueArousal) {
		this.valueArousal = valueArousal;
	}
	public int getValuePleasure() {
		return valuePleasure;
	}
	public void setValuePleasure(int valuePleasure) {
		this.valuePleasure = valuePleasure;
	}
	public int getValueDominance() {
		return valueDominance;
	}
	public void setValueDominance(int valueDominance) {
		this.valueDominance = valueDominance;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getReactTime() {
		return reactTime;
	}
	public void setReactTime(long reactTime) {
		this.reactTime = reactTime;
	}
	}