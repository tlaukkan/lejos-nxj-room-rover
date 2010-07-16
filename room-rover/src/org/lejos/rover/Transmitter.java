package org.lejos.rover;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.Colors.Color;

public class Transmitter implements Runnable {

	private Thread thread;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private MessageCoder messageCoder;
	
	private boolean disconnectRequest=false;
	private BTConnection connection;
	
	public Transmitter() {
		thread=new Thread(this);
	}
	
	public void listen() {
		thread.start();
	}

	@Override
	public void run() {
		
		RoomRover.getInstance().getLightSensor().setFloodlight(Color.BLUE);

		connection = Bluetooth.waitForConnection();
		inputStream = connection.openDataInputStream();
		outputStream = connection.openDataOutputStream();
		messageCoder = new MessageCoder(inputStream,outputStream);
		
		RoomRover.getInstance().getLightSensor().setFloodlight(Color.GREEN);

		while(!disconnectRequest) {
			try {
				messageCoder.decodeMessage();
			} catch (IOException e) {
				break;
			}
		}

		RoomRover.getInstance().getLightSensor().setFloodlight(Color.RED);

		connection.close();
	}

	public boolean isListening() {
		return thread.isAlive()&&connection==null;
	}

	public boolean isConnected() {
		return thread.isAlive()&&connection!=null;
	}

	public void disconnect() {
		disconnectRequest = true;
		connection.close();
		while(thread.isAlive()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
	}

	public MessageCoder getMessageCoder() {
		return messageCoder;
	}
	
}
