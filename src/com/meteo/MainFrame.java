package com.meteo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.meteo.models.CurrentWeather;
import com.meteo.utilities.Alert;
import com.meteo.utilities.Api;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String GENERIC_ERROR_MESSAGE = "An error occured try again !!";
	private static final String INTERNET_CONNECTIVITY_ERROR_MESSAGE = "error occured check your internet connection!!";
	
	private String foreCastUrl;
	private CurrentWeather currentWeather;
	
	private static final  Font DEFAULT_FONT = new Font("San Fransisco", Font.PLAIN,24);
	private static final Font LITTLE_FONT = new Font("San Fransisco", Font.PLAIN,14);
	private static final Color WHITE  = Color.WHITE;
	private static final Color BLUE = Color.decode("#8EA2C6");
	private static final Color LIGHT_WHITE = new Color(255,255,255,128);
	
	private JLabel locationLabel;
	private JLabel timeLabel;
	private JLabel temperatureLabel;
	private JPanel otherInfoLabel;
	private JLabel humidityLabel;
	private JLabel humidityValue;
	private JLabel precipLabel;
	private JLabel precipValue;
	private JLabel summaryLabel;

	public MainFrame(String title) {
		super(title);
		Container contentPane = getContentPane();
		setLayout(new BoxLayout(contentPane,BoxLayout.Y_AXIS));
		
		//set colors
		contentPane.setBackground(BLUE);
		
		//labels
		locationLabel = new JLabel("Luxembourg, LU", SwingConstants.CENTER);
		locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		locationLabel.setForeground(WHITE);
		locationLabel.setFont(new Font("San Fransisco", Font.PLAIN,24));
		
		timeLabel = new JLabel("... ", SwingConstants.CENTER);
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		timeLabel.setForeground(LIGHT_WHITE);
		timeLabel.setFont(DEFAULT_FONT);
		
		temperatureLabel = new JLabel("--", SwingConstants.CENTER);
		temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		temperatureLabel.setForeground(WHITE);
		temperatureLabel.setFont(new Font("San Fransisco", Font.PLAIN,160));
		
		otherInfoLabel = new JPanel(new GridLayout(2,2));
		otherInfoLabel.setBackground(BLUE);
		
		humidityLabel = new JLabel("humidity".toUpperCase(),SwingConstants.CENTER);
		humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		humidityLabel.setForeground(LIGHT_WHITE);
		humidityLabel.setFont(LITTLE_FONT);
		
		humidityValue = new JLabel("--", SwingConstants.CENTER);
		humidityValue.setAlignmentX(Component.CENTER_ALIGNMENT);
		humidityValue.setForeground(WHITE);
		humidityValue.setFont(DEFAULT_FONT);
		
		precipLabel = new JLabel("precipitation".toUpperCase(),SwingConstants.CENTER);
		precipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		precipLabel.setFont(LITTLE_FONT);
		precipLabel.setForeground(LIGHT_WHITE);
		
		precipValue = new JLabel("--",SwingConstants.CENTER);
		precipValue.setAlignmentX(Component.CENTER_ALIGNMENT);
		precipValue.setForeground(WHITE);
		precipValue.setFont(DEFAULT_FONT);
		
		otherInfoLabel.add(humidityLabel);
		otherInfoLabel.add(precipLabel);
		otherInfoLabel.add(humidityValue);
		otherInfoLabel.add(precipValue);
		
		summaryLabel = new JLabel("Récupération de la température actuelle...", SwingConstants.CENTER);
		summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		summaryLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		summaryLabel.setForeground(WHITE);
		summaryLabel.setFont(LITTLE_FONT);
		
		contentPane.add(locationLabel);
		contentPane.add(timeLabel);
		contentPane.add(temperatureLabel);
		contentPane.add(otherInfoLabel);
		contentPane.add(summaryLabel);
		
		//Darsky API request
		
		//String apiKey = "f220478ae9378bc20c642d06d213e27a";
		double latitude = 49.815273;
		double longitude = 6.129583;
		//foreCastUrl = "https://api.darksky.net/forecast/" + apiKey +  "/" + latitude + "," + longitude;
		
		//System.out.println(foreCastUrl);
		
		//System.out.println("avant la request");
		
		//Asynchronous Get Request with enqueue method
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(Api.getForecastUrl(latitude, longitude)).build();
		Call call = client.newCall(request);
		call.enqueue(new Callback() { 

			@Override
			public void onFailure(Call call, IOException e) {
				//add error box dialog
				Alert.error(MainFrame.this, "Error", INTERNET_CONNECTIVITY_ERROR_MESSAGE);
				//JOptionPane.showMessageDialog(MainFrame.this,"error occured check your internet connection!!", "error",JOptionPane.ERROR_MESSAGE);
				//System.err.println("error !! " + e.getMessage());	
			}

			@Override
			public void onResponse(Call call, Response response)   {
				System.out.println("thread : " + Thread.currentThread().getName());
				try {
				if(response.isSuccessful()) {
					//System.out.println(response.body().string());
					
					//Json format response
					String jsonData = response.body().string();
					currentWeather = getCurrentWeatherDetails(jsonData);
					
					EventQueue.invokeLater(() ->{
						updateScreen();
					});

					
					} 
					
				else {
					
					//error dialog box  : utility class
					Alert.error(MainFrame.this,GENERIC_ERROR_MESSAGE);
					//JOptionPane.showMessageDialog(MainFrame.this,"error occured !!", "error",JOptionPane.ERROR_MESSAGE);
					//System.err.println("error !! " + response.body().string());
				}
			}catch (ParseException | IOException e) {
				
				Alert.error(MainFrame.this,GENERIC_ERROR_MESSAGE);
				//e.printStackTrace();
			}
			}

			
		});
		
		
		/*new SwingWorker<String,Void>(){

			@Override
			protected String doInBackground() throws Exception {
				System.out.println("thread : " + Thread.currentThread().getName());
				
				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder().url(foreCastUrl).build();
				Call call = client.newCall(request);
				try {
					Response response = call.execute();
					return response.body().string();
			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("error: " + e);
				}
				return null;
			}
			
			@Override
			protected void done() {
				System.out.println("thread in done method : " + Thread.currentThread().getName());
				
				try {
					System.out.println(get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					System.err.println("error: " + e);
				}
			}
			
			}.execute();*/
		//new ForecastWorker(foreCastUrl).execute();
		
		//System.out.println("Après la request");
	}
	
	private CurrentWeather getCurrentWeatherDetails(String jsonData) throws ParseException {
		CurrentWeather currentWeather = new CurrentWeather();
		
		JSONObject forecast = (JSONObject) JSONValue.parseWithException(jsonData);
		String timezone = (String) forecast.get("timezone");
		JSONObject currently = (JSONObject) forecast.get("currently");
		
		
		currentWeather.setTemperature((double) currently.get("temperature"));
		currentWeather.setTimezone((String) forecast.get("timezone"));
		currentWeather.setTime((long) currently.get("time"));
		currentWeather.setHumidity((double)currently.get("humidity"));
		
		//no cast because of null values, must use parseDouble method to convert with an empty string
		//double precipProbability = Double.parseDouble(currently.get("precipProbability") + "");
		currentWeather.setPrecipProbability(Double.parseDouble(currently.get("precipProbability") + ""));
		currentWeather.setSummary((String)currently.get("summary"));
		return currentWeather;
		
		
	}
	
	protected void updateScreen() {
		timeLabel.setText("Time : " + currentWeather.getFormattedTime() + " Temperature : ");
		temperatureLabel.setText(currentWeather.getTemperature() + "°");
		humidityValue.setText(currentWeather.getHumidity() + "");
		precipValue.setText(currentWeather.getPrecipProbability() + "%");
		summaryLabel.setText(currentWeather.getSummary());
		
	}
	
	//Sizes
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500,500);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	// Synchronous Get Request with SwingWorker class
		/*class ForecastWorker extends SwingWorker<String,Void>{
			
			private String foreCastUrl;
			
			public ForecastWorker(String foreCastUrl) {
				this.foreCastUrl = foreCastUrl;
			}

			@Override
			protected String doInBackground() throws Exception {
				System.out.println("thread : " + Thread.currentThread().getName());
				
				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder().url(foreCastUrl).build();
				Call call = client.newCall(request);
				try {
					Response response = call.execute();
					if(response.isSuccessful()) {
						
						return response.body().string();
					}
			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("error: " + e);
				}
				return null;
			}
			
			//callBack method
			@Override
			protected void done() {
				System.out.println("thread in done method : " + Thread.currentThread().getName());
				
				try {
					System.out.println(get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					System.err.println("error: " + e);
				}
			}
		}*/
}

