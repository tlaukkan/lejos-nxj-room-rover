package org.lejos.rover.remote;

public interface LinkListener {
	
	public void bluetoothInitialized(RemoteLink remote);
	public void bluetoothFailed(RemoteLink remote);
	public void searchStarted(RemoteLink remote);	
	public void searchCompleted(RemoteLink remote);
	public void searchFailed(RemoteLink roverRemote);
	public void connectStarted(RemoteLink remote);
	public void connectCompleted(RemoteLink remote);
	public void connectFailed(RemoteLink remote);
	public void disconnected(RemoteLink remote);
	
}
