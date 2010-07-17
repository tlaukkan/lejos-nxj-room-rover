package org.lejos.rover.remote;

public interface MessageListener {
	public void keepalive();
	public void radarPing(int x, int y,int angle, int distance);
}
