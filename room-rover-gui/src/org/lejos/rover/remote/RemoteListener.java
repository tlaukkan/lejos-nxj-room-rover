package org.lejos.rover.remote;

public interface RemoteListener {
	
	public void bluetoothInitialized(RoverRemote remote);
	public void bluetoothFailed(RoverRemote remote);
	public void searchStarted(RoverRemote remote);	
	public void searchCompleted(RoverRemote remote);
	public void searchFailed(RoverRemote roverRemote);
	public void roverConnectStarted(RoverRemote remote);
	public void roverConnectCompleted(RoverRemote remote);
	public void roverConnectFailed(RoverRemote remote);
	public void roverDisconnected(RoverRemote remote);
	
}
