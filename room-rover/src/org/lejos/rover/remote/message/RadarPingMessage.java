package org.lejos.rover.remote.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RadarPingMessage extends Message {

	public static int TYPE=20;	
	
	private int x;
	private int y; 
	private int angle; 
	private int distance;
	
	public RadarPingMessage() {
		super(TYPE);
	}
		
	public RadarPingMessage(int x, int y, int angle, int distance) {
		super(TYPE);
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.distance = distance;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAngle() {
		return angle;
	}
	
	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public void read(DataInputStream inputStream) throws IOException {
		x=inputStream.readInt();
		y=inputStream.readInt();
		angle=inputStream.readInt();
		distance=inputStream.readInt();
	}

	@Override
	public void write(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt(x);
		outputStream.writeInt(y);
		outputStream.writeInt(angle);
		outputStream.writeInt(distance);
	}

}
