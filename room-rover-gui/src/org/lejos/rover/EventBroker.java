package org.lejos.rover;

import org.lejos.rover.remote.MessageListener;
import org.lejos.rover.remote.RemoteListener;
import org.lejos.rover.remote.RoverRemote;
import org.lejos.rover.remote.message.KeepaliveMessage;
import org.lejos.rover.remote.message.Message;
import org.lejos.rover.remote.message.RadarPingMessage;

public class EventBroker implements RemoteListener,MessageListener {

	@Override
	public void bluetoothFailed(RoverRemote remote) {
	}

	@Override
	public void bluetoothInitialized(RoverRemote remote) {
	}

	@Override
	public void roverConnectCompleted(RoverRemote remote) {
	}

	@Override
	public void roverConnectFailed(RoverRemote remote) {
	}

	@Override
	public void roverConnectStarted(RoverRemote remote) {
	}

	@Override
	public void roverDisconnected(RoverRemote remote) {
	}

	@Override
	public void searchCompleted(RoverRemote remote) {
	}

	@Override
	public void searchFailed(RoverRemote roverRemote) {		
	}

	@Override
	public void searchStarted(RoverRemote remote) {
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
