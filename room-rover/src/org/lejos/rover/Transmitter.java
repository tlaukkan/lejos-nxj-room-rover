package org.lejos.rover;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
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
		
		try {
	
			while(connection==null&&!disconnectRequest) {
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
			
			inputStream = connection.openDataInputStream();
			outputStream = connection.openDataOutputStream();
			messageCoder = new MessageCoder(inputStream,outputStream);
			
			RoomRover.getInstance().getLightSensor().setFloodlight(Color.GREEN);
	
			while(!disconnectRequest) {
				try {
					messageCoder.decodeMessage();
				} catch (IOException e) {
					messageCoder.disconnect();
					try {
						inputStream.close();
					} catch (IOException ex) {
					}
					try {
						outputStream.close();
					} catch (IOException ex) {
					}					
					//connection.close();
					break;
				}
			}
	
		} 
		catch (Throwable t) {
			System.out.println(t.toString());
		}

		messageCoder=null;
		inputStream=null;
		outputStream=null;
		connection=null;
		RoomRover.getInstance().getLightSensor().setFloodlight(Color.RED);
	}

	public boolean isListening() {
		return thread!=null&&thread.isAlive()&&connection==null;
	}

	public boolean isConnected() {
		return thread!=null&&thread.isAlive()&&connection!=null;
	}

	public void disconnect() {
		disconnectRequest = true;

		if(messageCoder!=null) {
			messageCoder.disconnect();
		}
		
		if(inputStream!=null) {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}

		if(outputStream!=null) {
			try {
				outputStream.close();
			} catch (IOException e) {
			}
		}
		
		if(connection!=null) {
			connection.close();
		}
				
		while(thread!=null&&thread.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		
	}

	public MessageCoder getMessageCoder() {
		return messageCoder;
	}
	
}
