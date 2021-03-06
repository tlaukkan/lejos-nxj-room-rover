package org.lejos.rover.remote;

import java.util.ArrayList;

import org.lejos.rover.remote.message.KeepaliveMessage;
import org.lejos.rover.remote.message.Message;

@SuppressWarnings("unchecked")
public class RemoteLink implements Runnable {

	private Thread thread;
	private Transmitter transmitter;
	private boolean stopRequest = false;
	
	protected ArrayList listeners = new ArrayList();
	private ArrayList messagesToSend=new ArrayList();

	public RemoteLink() {
		thread = new Thread(this);
	}

	public void sendMessage(Message message) {
		synchronized(messagesToSend) {
			if(isConnected()) {
				messagesToSend.add(message);
			}
		}
	}

	public void addMessageListener(MessageListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeMessageListener(MessageListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	public void start() {
		thread.start();
	}

	@Override
	public void run() {

		while (!stopRequest) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			
			transmitter = new Transmitter(this);

			transmitter.listen();

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}

			while (transmitter.isListening()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}

			while (transmitter.isConnected()) {
				
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

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}

			}

			transmitter = null;
			
			synchronized(messagesToSend) {
				messagesToSend.clear();
			}

		}

	}

	public boolean isConnected() {
		return transmitter != null && transmitter.isConnected();
	}

	public void stop() {
		stopRequest = true;
		transmitter.disconnect();

		while (thread.isAlive()) {
			thread.interrupt();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

}
