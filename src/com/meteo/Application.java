package com.meteo;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Application {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
			
				MainFrame main = new MainFrame("meteo");
				main.setResizable(false);
				main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//pack method : managed by LayoutManager
				main.pack();
				main.setLocationRelativeTo(null);
				main.setVisible(true);
			}
			
		});
		
		
		

	}

}
