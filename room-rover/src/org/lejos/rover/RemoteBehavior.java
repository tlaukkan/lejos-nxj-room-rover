package org.lejos.rover;

import lejos.robotics.Colors.Color;
import lejos.robotics.subsumption.Behavior;

public class RemoteBehavior implements Behavior, Runnable {

	private boolean suppressRequest=false;
	private Thread thread;
	
	@Override
	public void action() {
		if(thread==null) {
			suppressRequest=false;
			thread=new Thread(this);
			thread.start();
		}
	}

	@Override
	public void suppress() {
		if(thread!=null) {
			suppressRequest=true;
			while(thread.isAlive()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void run() {
		boolean lightOn=false;
		
		while(!suppressRequest) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			
		}
		
	}

}
