package org.lejos.rover;

import javax.swing.JFrame;

public class RoomRoverGui {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		JFrame frame=new JFrame("Room Rover GUI");
		frame.setLocationByPlatform(true);
		frame.setSize(800, 800);
		frame.setVisible(true);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frame.setVisible(false);
		System.exit(0);
		
	}

}
