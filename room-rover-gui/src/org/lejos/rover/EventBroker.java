package org.lejos.rover;

import org.lejos.rover.remote.MessageListener;
import org.lejos.rover.remote.LinkListener;
import org.lejos.rover.remote.RemoteLink;
import org.lejos.rover.remote.message.KeepaliveMessage;
import org.lejos.rover.remote.message.Message;
import org.lejos.rover.remote.message.RadarPingMessage;

public class EventBroker implements LinkListener,MessageListener {

	@Override
	public void bluetoothFailed(RemoteLink remote) {
	}

	@Override
	public void bluetoothInitialized(RemoteLink remote) {
	}

	@Override
	public void connectCompleted(RemoteLink remote) {
	}

	@Override
	public void connectFailed(RemoteLink remote) {
	}

	@Override
	public void connectStarted(RemoteLink remote) {
	}

	@Override
	public void disconnected(RemoteLink remote) {
	}

	@Override
	public void searchCompleted(RemoteLink remote) {
	}

	@Override
	public void searchFailed(RemoteLink roverRemote) {		
	}

	@Override
	public void searchStarted(RemoteLink remote) {
	}

	@Override
	public void messageReceived(Message message) {
		if(message.getType()==KeepaliveMessage.TYPE) {
			System.out.print(".");
		}

		if(message.getType()==RadarPingMessage.TYPE) {
			RadarPingMessage ping=(RadarPingMessage) message;
			System.out.println("Radar ping x: "+ping.getX()+" y :"+ping.getY()+" a: "+ping.getAngle()+" d: "+ping.getDistance());
		}
	}

}
