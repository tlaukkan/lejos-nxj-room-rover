package org.lejos.rover;

import org.lejos.rover.remote.MessageListener;
import org.lejos.rover.remote.RemoteListener;
import org.lejos.rover.remote.RoverRemote;

public class EventBroker implements RemoteListener,MessageListener {

	@Override
	public void keepalive() {
		System.out.print(".");
	}

	@Override
	public void radarPing(int x, int y, int angle, int distance) {
		System.out.println("Radar ping x: "+x+" y :"+y+" a: "+angle+" d: "+distance);
	}

	@Override
	public void bluetoothFailed(RoverRemote remote) {
	}

	@Override
	public void bluetoothInitialized(RoverRemote remote) {
	}

	@Override
	public void roverConnectCompleted(RoverRemote remote) {
		remote.getTransmitter().getMessageCoder().addMessageListener(this);
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

}
