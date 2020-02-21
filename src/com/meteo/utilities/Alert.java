package com.meteo.utilities;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.meteo.MainFrame;

public class Alert {

	public static void error(Component parentComponent,String title, String message) {
		JOptionPane.showMessageDialog(parentComponent,message, title,JOptionPane.ERROR_MESSAGE);
	}
	
	public static void error(Component parentComponent, String message) {
		error(parentComponent, "Error", message);
	}
	
	public static void error(Component parentComponent) {
		error(parentComponent,"An Error occured");
	}
}
