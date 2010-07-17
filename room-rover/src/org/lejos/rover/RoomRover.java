package org.lejos.rover;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.ColorLightSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

import org.lejos.rover.radar.Radar;
import org.lejos.rover.remote.RemoteLink;

public class RoomRover {
	
	private static RoomRover rover;

	private Arbitrator arbitrator;
	private EventBroker eventBroker;
	
	private RemoteLink remoteLink;
	private Radar radar;
	
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
				//new InactiveBehavior(),
				//new RemoteBehavior()
		};
		
		lightSensor = new ColorLightSensor(SensorPort.S2,ColorLightSensor.TYPE_COLORNONE);
		
		eventBroker=new EventBroker();

		remoteLink=new RemoteLink();
		
		radar = new Radar(new UltrasonicSensor(SensorPort.S3), Motor.A);

		radar.addRadarListener(eventBroker);

		arbitrator = new Arbitrator(behaviors);		
		
	}
	
	protected void start() {
		LCD.drawString("Room Rover 0.2", 0, 0);
		remoteLink.start();
		radar.start();
		arbitrator.start();
	}
	
	protected void stop() {
		radar.stop();
		remoteLink.stop();
	}
			
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
		    	  Thread thread=new Thread(new Runnable() {					
		    		  	@Override
						public void run() {
		    		  		RoomRover.getInstance().stop();
						}
		    	  });
		    	  
		    	  thread.start();
		    	  
		    	  for(int i=0;i<20;i++) {
		    		if(!thread.isAlive()) {
		    			break;
		    		}
		    		try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
		    	  }
		    	  
		    	  System.exit(0);
		      }
		    });
		
		rover.start();		
	}

}
