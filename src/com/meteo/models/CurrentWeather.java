package com.meteo.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {

	private String timezone;
	private double temperature;
	private double humidity;
	private double precipProbability;
	private long time;
	private String summary;
	
	public String getTimezone() {
		return timezone;
	}
	
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	public int getTemperature() {
		return (int)Math.round(temperature);
	}
	
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	public double getHumidity() {
		return humidity;
	}
	
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	
	public int getPrecipProbability() {
		return (int)Math.round(precipProbability);
	}
	
	public void setPrecipProbability(double precipProbability) {
		this.precipProbability = precipProbability;
	}
	
	public long getTime() {
		return time;
	}
	
	public String getFormattedTime() {
		//time conversion
		Date date = new Date(time * 1000L);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		formatter.setTimeZone(TimeZone.getTimeZone(timezone));
		String StringTime = formatter.format(date);
		return StringTime;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
}
