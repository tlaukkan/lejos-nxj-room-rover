package org.lejos.rover.remote;

import java.util.ArrayList;

import org.lejos.rover.remote.message.KeepaliveMessage;
import org.lejos.rover.remote.message.Message;

@SuppressWarnings("unchecked")
public class RemoteLink implements Runnable {

	private Thread thread;
	private Transmitter transmitter;
	private boolean stopRequest = false;
	
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
			
			transmitter = new Transmitter();

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
