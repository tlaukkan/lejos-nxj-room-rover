package org.lejos.rover.remote;

import java.util.ArrayList;
import java.util.List;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

import org.lejos.rover.remote.message.KeepaliveMessage;
import org.lejos.rover.remote.message.Message;

public class RemoteLink implements Runnable {
	
	private NXTComm communicator;

	private List<LinkListener> listeners=new ArrayList<LinkListener>();
	protected List<MessageListener> messageListeners=new ArrayList<MessageListener>();
	private List<Message> messagesToSend=new ArrayList<Message>();

	
	private Thread thread;
	private NXTInfo[] availableRovers;
	private NXTInfo targetRover;
	
	private Transmitter transmitter;
	
	private boolean stopRequest=false;
	
	public RemoteLink() {
		thread=new Thread(this,"RemoteLink");
	}
	
	public void start() {		
		thread.start();		
	}
	
	public void stop() {
		stopRequest=true;
	}
	
	public void sendMessage(Message message) {
		synchronized(messagesToSend) {
			if(isConnected()) {
				messagesToSend.add(message);
			}
		}
	}

	public void addMessageListener(MessageListener listener) {
		synchronized (messageListeners) {
			messageListeners.add(listener);
		}
	}

	public void removeMessageListener(MessageListener listener) {
		synchronized (messageListeners) {
			messageListeners.remove(listener);
		}
	}
	
	public void addRemoteListener(LinkListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeRemoteListener(LinkListener listener) {
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
		
		Transmitter newTransmitter=new Transmitter(this,communicator,targetRover);
		synchronized(listeners) {
			for(LinkListener listener : listeners) {
				listener.connectStarted(this);
			}
		}
		if(newTransmitter.connect()) {
			transmitter=newTransmitter;
			synchronized(listeners) {
				for(LinkListener listener : listeners) {
					listener.connectCompleted(this);
				}
			}
			return true;
		} 
		else {
			synchronized(listeners) {
				for(LinkListener listener : listeners) {
					listener.connectFailed(this);
				}
			}
			return false;
		}		
	}
	
	public void disconnect() {
		if(transmitter!=null&&transmitter.isConnected()) {
			transmitter.disconnect();
		}
	}
	
	@Override
	public void run() {
		try {
			communicator = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			synchronized(listeners) {
				for(LinkListener listener : listeners) {
					listener.bluetoothInitialized(this);
				}
			}		
		}
		catch(Throwable t) {
			t.printStackTrace();
			synchronized(listeners) {
				for(LinkListener listener : listeners) {
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
						for(LinkListener listener : listeners) {
							listener.searchStarted(this);
						}
					}
					availableRovers=communicator.search(null,NXTCommFactory.BLUETOOTH);
					synchronized(listeners) {
						for(LinkListener listener : listeners) {
							listener.searchCompleted(this);
						}
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					synchronized(listeners) {
						for(LinkListener listener : listeners) {
							listener.searchFailed(this);
						}
					}
				}
				lastSearchTime=System.currentTimeMillis();
			}
			
			if(transmitter!=null&&!transmitter.isConnected()) {
				transmitter=null;
				synchronized(messagesToSend) {
					messagesToSend.clear();
				}
				synchronized(listeners) {
					for(LinkListener listener : listeners) {
						listener.disconnected(this);
					}
				}				
			}
			
			if(isConnected()) {
				transmitter.encodeMessage(new KeepaliveMessage());
				
				while(messagesToSend.size()>0) {
					Message message=null;
					synchronized(messagesToSend) {
						message=(Message)messagesToSend.remove(0);
					}
					transmitter.encodeMessage(message);
				}
								
				// Check for connection timeout.
				if(transmitter.getLastReceiveTime()!=0&&System.currentTimeMillis()-transmitter.getLastReceiveTime()>5000) {
					transmitter.disconnect();
				}
			}
									
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
