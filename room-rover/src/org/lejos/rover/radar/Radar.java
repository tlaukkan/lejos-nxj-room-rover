package org.lejos.rover.radar;
import java.util.Vector;

import lejos.nxt.*;
import lejos.robotics.TachoMotor;

public class Radar implements Runnable {

	private Thread thread;
	private boolean stopRequest;
	private Vector radarListeners=new Vector();
	
	private UltrasonicSensor sonic;
	private TachoMotor motor;

	private double fieldOfView=90;
	private double motorToRadarAngleRatio=1.8;
	private int radarSweepSpeed=30;
	
	public double getFieldOfView() {
		return fieldOfView;
	}

	public void setFieldOfView(double fieldOfView) {
		this.fieldOfView = fieldOfView;
	}

	public double getMotorToRadarAngleRatio() {
		return motorToRadarAngleRatio;
	}

	public void setMotorToRadarAngleRatio(double motorToRadarAngleRatio) {
		this.motorToRadarAngleRatio = motorToRadarAngleRatio;
	}

	public int getRadarSweepSpeed() {
		return radarSweepSpeed;
	}

	public void setRadarSweepSpeed(int radarMotorSweepPower) {
		this.radarSweepSpeed = radarMotorSweepPower;
	}
	
	public Radar(UltrasonicSensor sonic, TachoMotor motor) {
		this.sonic = sonic;
		this.motor = motor;
	}

	public void addRadarListener(RadarListener listener) {
		synchronized(radarListeners) {
			radarListeners.addElement(listener);
		}
	}
	
	public void removeRadarListener(RadarListener listener) {
		synchronized(radarListeners) {
			radarListeners.removeElement(listener);
		}
	}
	
	public void start() {
		if (isRunning()) {
			throw new RuntimeException("Radar already running.");
		}
		this.thread = new Thread(this);
		this.thread.start();
		this.stopRequest = false;
	}

	public void stop() {
		stopRequest = true;
	}

	public boolean isRunning() {
		return thread != null && thread.isAlive();
	}

	@Override
	public void run() {
		
		synchronized(radarListeners) {
			for(int i=0;i<radarListeners.size();i++) {
				((RadarListener)radarListeners.elementAt(i)).radarStarted();
			}
		}
		
		motor.resetTachoCount();
		
		motor.smoothAcceleration(true);
		motor.regulateSpeed(true);

		motor.setSpeed(360);
		motor.rotateTo((int) (-motorToRadarAngleRatio * fieldOfView/2), false);

		int direction=1;
		while (!stopRequest) {
  			motor.setSpeed((int)(radarSweepSpeed*motorToRadarAngleRatio));
			motor.rotateTo((int) (direction * motorToRadarAngleRatio * fieldOfView/2), true);

			long startTime = System.currentTimeMillis();
			int lastAngle=-1000;
			while (!stopRequest) {				
				long currentTime = System.currentTimeMillis();
				
				long timeSpent = (currentTime - startTime);
				int currentAngle = (int) (motor.getTachoCount() / motorToRadarAngleRatio);
				if(currentAngle==lastAngle) {
					if (!motor.isMoving()) {
						break;
					}
					continue;
				}
					
				lastAngle=currentAngle;
				int currentDistance = sonic.getDistance();
				
				RadarPingEvent pingEvent=new RadarPingEvent();
				pingEvent.setTime(timeSpent);
				pingEvent.setAngle(currentAngle);
				pingEvent.setDistance(currentDistance);
				
				synchronized(radarListeners) {
					for(int i=0;i<radarListeners.size();i++) {
						((RadarListener)radarListeners.elementAt(i)).radarPinged(pingEvent);
					}
				}

			}

			direction*=-1;
		}

		motor.setSpeed(360);
		motor.rotateTo(0, false);

		thread = null;
		
		synchronized(radarListeners) {
			for(int i=0;i<radarListeners.size();i++) {
				((RadarListener)radarListeners.elementAt(i)).radarStopped();
			}
		}

	}

}
