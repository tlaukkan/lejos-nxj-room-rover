package org.lejos.rover.remote;

import java.util.ArrayList;
import java.util.List;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class RoverRemote implements Runnable {
	
	private NXTComm communicator;

	private List<RemoteListener> listeners=new ArrayList<RemoteListener>();
	private Thread thread;
	private NXTInfo[] availableRovers;
	private NXTInfo targetRover;
	
	private Transmitter transmitter;
	
	private boolean stopRequest=false;
	
	public RoverRemote() {
		thread=new Thread(this,"RoverRemote");
	}
	
	public void start() {		
		thread.start();		
	}
	
	public void stop() {
		stopRequest=true;
	}
	
	public void addRemoteListener(RemoteListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeRemoteListener(RemoteListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}
	
	public void search() {
		availableRovers=null;
	}
	
	public NXTInfo[] getAvailableRovers() {
		return availableRovers;
	}
	
	public boolean isConnected() {
		return transmitter!=null&&transmitter.isConnected();
	}

	public boolean connect() {
		if(transmitter!=null||targetRover==null) {
			return false;
		}
		
		Transmitter newTransmitter=new Transmitter(communicator,targetRover);
		synchronized(listeners) {
			for(RemoteListener listener : listeners) {
				listener.roverConnectStarted(this);
			}
		}
		if(newTransmitter.connect()) {
			transmitter=newTransmitter;
			synchronized(listeners) {
				for(RemoteListener listener : listeners) {
					listener.roverConnectCompleted(this);
				}
			}
			return true;
		} 
		else {
			synchronized(listeners) {
				for(RemoteListener listener : listeners) {
					listener.roverConnectFailed(this);
				}
			}
			return false;
		}		
	}
	
	public void disconnect() {
		if(transmitter!=null) {
			transmitter.disconnect();
		}
	}
	
	@Override
	public void run() {
		try {
			communicator = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			synchronized(listeners) {
				for(RemoteListener listener : listeners) {
					listener.bluetoothInitialized(this);
				}
			}		
		}
		catch(Throwable t) {
			t.printStackTrace();
			synchronized(listeners) {
				for(RemoteListener listener : listeners) {
					listener.bluetoothFailed(this);
				}
			}					
		}
		
		long lastSearchTime=0;
		while(!stopRequest) {
					
			long currentTime=System.currentTimeMillis();
			
			if(!isConnected()&&(availableRovers==null||(availableRovers.length==0&&currentTime-lastSearchTime>5000))) {
				try {					
					synchronized(listeners) {
						for(RemoteListener listener : listeners) {
							listener.searchStarted(this);
						}
					}
					availableRovers=communicator.search(null,NXTCommFactory.BLUETOOTH);
					synchronized(listeners) {
						for(RemoteListener listener : listeners) {
							listener.searchCompleted(this);
						}
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					synchronized(listeners) {
						for(RemoteListener listener : listeners) {
							listener.searchFailed(this);
						}
					}
				}
				lastSearchTime=System.currentTimeMillis();
			}
			
			if(transmitter!=null&&!transmitter.isConnected()) {
				transmitter=null;
				synchronized(listeners) {
					for(RemoteListener listener : listeners) {
						listener.roverDisconnected(this);
					}
				}				
			}
			
			if(isConnected()) {
				transmitter.sendKeepAlive();
			}
									
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(transmitter!=null&&transmitter.isConnected()) {
			transmitter.disconnect();
			transmitter=null;
		}
		
	}

	public NXTInfo getTargetRover() {
		return targetRover;
	}

	public void setTargetRover(NXTInfo targetRover) {
		this.targetRover = targetRover;
	}

	public Transmitter getTransmitter() {
		return transmitter;
	}
	
}
