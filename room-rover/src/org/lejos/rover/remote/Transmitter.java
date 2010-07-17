package org.lejos.rover.remote;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.Colors.Color;

import org.lejos.rover.RoomRover;

public class Transmitter implements Runnable {

	private boolean stopRequest=false;

	private Thread thread;
	private MessageCoder messageCoder;
	private BTConnection connection;
	
	public MessageCoder getMessageCoder() {
		return messageCoder;
	}

	public Transmitter() {
		thread=new Thread(this);
	}
	
	public void listen() {
		thread.start();
	}

	@Override
	public void run() {
		RoomRover.getInstance().getLightSensor().setFloodlight(Color.BLUE);
		
		try {
	
			while(connection==null&&!stopRequest) {
				try {
					//connection = Bluetooth.waitForConnection();
					connection = Bluetooth.waitForConnection(5000,NXTConnection.PACKET);
				}
				catch(Throwable t) {
					
				}
			}
			
			if(connection==null) {
				return;
			}
			
			messageCoder = new MessageCoder(connection.openDataInputStream(),connection.openDataOutputStream());
			
			RoomRover.getInstance().getLightSensor().setFloodlight(Color.GREEN);
	
			while(messageCoder.isConnected()) {
				messageCoder.decodeMessage();
			}
	
		} 
		catch (Throwable t) {
			System.out.println(t.toString());
		}

		messageCoder=null;
		connection=null;
		
		RoomRover.getInstance().getLightSensor().setFloodlight(Color.RED);
	}

	public boolean isListening() {
		return (thread!=null&&thread.isAlive())&&(messageCoder==null||!messageCoder.isConnected());
	}

	public boolean isConnected() {
		return (thread!=null&&thread.isAlive())&&(messageCoder!=null&&messageCoder.isConnected());
	}

	public void disconnect() {
		stopRequest = true;

		if(messageCoder!=null) {
			messageCoder.disconnect();
		}

		if(connection!=null) {
			synchronized(messageCoder) {
				connection.close();
			}
		}

		while(thread!=null&&thread.isAlive()) {
			
			thread.interrupt();

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		
	}
	
}
