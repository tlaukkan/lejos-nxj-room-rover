package org.lejos.rover;

import lejos.robotics.Colors.Color;
import lejos.robotics.subsumption.Behavior;

public class RemoteBehavior implements Behavior,Runnable {

	private Thread thread;
	private Transmitter transmitter;

	/**
	 * Controls thread access to hasControl field. Has to be acquired as well when RemoteBehavior accesses
	 * shared resources like motors.
	 */
	private Object controlLock=new Object();
	private boolean hasControl=false;
	private boolean takeControl=false;
	private boolean releaseControl=false;
	
	public RemoteBehavior() {
		thread=new Thread(this);
		thread.start();
	}
	
	@Override
	public void action() {
		hasControl=true;
	}

	@Override
	public void suppress() {
		releaseControl=true;
		while(true) {
			synchronized(controlLock) {				
				if(!hasControl) {
					break;
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		releaseControl=false;
	}

	@Override
	public boolean takeControl() {
		return takeControl;
	}

	@Override
	public void run() {
		
		while(true) {
			
			takeControl=false;
			hasControl=false;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}					

			
			try {
				transmitter=new Transmitter();

				boolean lightOn=false;
				while(transmitter.isListening()) {

					if(lightOn) {
						RoomRover.getInstance().getLightSensor().setFloodlight(false);
						lightOn=false;
					} else {
						RoomRover.getInstance().getLightSensor().setFloodlight(Color.RED);						
						lightOn=true;
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
				}
				
				takeControl=true;
				
				while(transmitter.isConnected()) {

					synchronized(controlLock) {
						if(hasControl) {
							if(lightOn) {
								RoomRover.getInstance().getLightSensor().setFloodlight(false);
								lightOn=false;
							} else {
								RoomRover.getInstance().getLightSensor().setFloodlight(Color.RED);						
								lightOn=true;
							}
							if(releaseControl) {
								hasControl=false;
							}
						}
					}
					
					transmitter.getMessageCoder().encodeKeepalive();

					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {}
				}
				
			}
			catch(Throwable t) {
				while(transmitter!=null&&transmitter.isConnected()) {
					transmitter.setDisconnectRequest(true);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {}					
				}
			}
			
			transmitter=null;
			
		}
		
	}

	public Object getControlLock() {
		return controlLock;
	}

	public boolean hasControl() {
		return hasControl;
	}
	
	public MessageCoder getMessageCoder() {
		Transmitter trans=transmitter;
		if(trans!=null) {
			return trans.getMessageCoder();
		} else {
			return null;
		}
	}

}
