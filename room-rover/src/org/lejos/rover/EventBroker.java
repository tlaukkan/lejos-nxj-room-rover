package org.lejos.rover;

import org.lejos.rover.radar.RadarListener;
import org.lejos.rover.radar.RadarPingEvent;
import org.lejos.rover.remote.message.RadarPingMessage;

public class EventBroker implements RadarListener{

	@Override
	public void radarStarted() {
	}

	@Override
	public void radarStopped() {
	}

	@Override
	public void radarPinged(RadarPingEvent event) {
		RoomRover.getInstance().getRemoteLink().sendMessage(new RadarPingMessage(0,0,event.getAngle(),event.getDistance()));
	}

	
}
