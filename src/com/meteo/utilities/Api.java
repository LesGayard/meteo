package com.meteo.utilities;

public class Api {

	private static final String FORECAST_API_BASE_URL = "https://api.darksky.net/forecast/";
	private static final String FORECAST_API_KEY = "f220478ae9378bc20c642d06d213e27a";
	
	public static String getForecastUrl(double latitude, double longitude) {
		return FORECAST_API_BASE_URL + FORECAST_API_KEY +  "/" + latitude + "," + longitude + "?units=si";
	}
}
