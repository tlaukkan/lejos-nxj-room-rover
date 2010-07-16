package org.lejos.rover;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.ColorLightSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.Colors.Color;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class RoomRover {

	private Arbitrator arbitrator;
	private ColorLightSensor lightSensor;

	public Arbitrator getArbitrator() {
		return arbitrator;
	}

	public ColorLightSensor getLightSensor() {
		return lightSensor;
	}
	
	public RoomRover() {
		
		lightSensor = new ColorLightSensor(SensorPort.S2,ColorLightSensor.TYPE_COLORNONE);

		Behavior[] behaviors=new Behavior[] {
				new RemoteBehavior(),
				new InactiveBehavior()
		};
		arbitrator = new Arbitrator(behaviors);
	}
	
	public void start() {
		LCD.drawString("Room Rover 0.1", 0, 0);
		arbitrator.start();
	}
		
	private static RoomRover rover;
	
	public static RoomRover getInstance() {
		return rover;
	}
	
	public static void main(String[] args) {
		rover=new RoomRover();

		Button.ESCAPE.addButtonListener(new ButtonListener() {
		      public void buttonPressed(Button b) {
		        LCD.drawString("ESCAPE pressed", 0, 0);
		      }

		      public void buttonReleased(Button b) {
		    	  System.exit(0);
		      }
		    });
		
		rover.start();		
	}

}
