package org.lejos.rover;

import java.io.IOException;

import org.lejos.rover.radar.RadarListener;
import org.lejos.rover.radar.RadarPingEvent;

public class EventBroker implements RadarListener{

	@Override
	public void radarStarted() {
	}

	@Override
	public void radarStopped() {
	}

	@Override
	public void radarPinged(RadarPingEvent event) {
		/*try {
			MessageCoder coder=RoomRover.getInstance().getRemoteLink().getMessageCoder();
			if(coder!=null) {
				coder.encodeRadarPing(0, 0, event.getAngle(), event.getDistance());
			}
		} catch (Throwable t) {
		}*/
	}

	
}
