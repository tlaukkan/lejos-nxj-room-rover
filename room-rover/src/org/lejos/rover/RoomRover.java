package org.lejos.rover;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.ColorLightSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class RoomRover {

	private Arbitrator arbitrator;
	private RemoteLink remoteLink;
	
	private ColorLightSensor lightSensor;
		
	public Arbitrator getArbitrator() {
		return arbitrator;
	}
	
	public RemoteLink getRemoteLink() {
		return remoteLink;
	}

	public ColorLightSensor getLightSensor() {
		return lightSensor;
	}
	
	public RoomRover() {
		
		Behavior[] behaviors=new Behavior[] {
				new InactiveBehavior(),
				new RemoteBehavior()
		};
		arbitrator = new Arbitrator(behaviors);		
		remoteLink=new RemoteLink();
		lightSensor = new ColorLightSensor(SensorPort.S2,ColorLightSensor.TYPE_COLORNONE);

	}
	
	protected void start() {
		LCD.drawString("Room Rover 0.2", 0, 0);
		remoteLink.start();
		arbitrator.start();
	}
	
	protected void stop() {
		remoteLink.stop();
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
		    	  rover.stop();
		    	  System.exit(0);
		      }
		    });
		
		rover.start();		
	}

}
