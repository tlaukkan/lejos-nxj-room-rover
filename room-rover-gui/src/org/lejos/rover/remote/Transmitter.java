package org.lejos.rover.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;

import org.lejos.rover.remote.message.Message;
import org.lejos.rover.remote.message.MessageFactory;

public class Transmitter implements Runnable {

	private RoverRemote remote;
	private Thread thread;
	private NXTComm communicator;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private NXTInfo nxt;
	private boolean stopRequest;
	private long lastReceiveTime;

	public void setStopRequest(boolean stopRequest) {
		this.stopRequest = stopRequest;
	}

	public Transmitter(RoverRemote remote, NXTComm communicator, NXTInfo nxt) {
		this.remote=remote;
		this.communicator=communicator;
		this.nxt=nxt;
	}
	
	public boolean connect() {
		try {
			if(!communicator.open(nxt)) {
				return false;
			}
		} catch (NXTCommException e) {
			e.printStackTrace();
			return false;
		}

		inputStream=new DataInputStream(communicator.getInputStream());
		outputStream=new DataOutputStream(communicator.getOutputStream());
		
		thread=new Thread(this);
		thread.start();
		return true;
	}
	
	public void disconnect() {
		stopRequest=true;
		try {
			communicator.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return thread.isAlive();
	}

	@Override
	public void run() {		
		
		lastReceiveTime = System.currentTimeMillis();
		
		while(!stopRequest) {
			
			try {
				
				int messageType = inputStream.readUnsignedByte();
				
				Message message = MessageFactory.constructMessage(messageType);
				if(message!=null) {
					message.read(inputStream);
	
					synchronized (this.remote.messageListeners) {
						for (MessageListener listener : this.remote.messageListeners) {
							listener.messageReceived(message);
						}
					}
				}
				else {
					stopRequest=true; // Unknown message type. Probably transmission error.				e.printStackTrace();
				}
				
			} catch (IOException e) {
				stopRequest=true;
				e.printStackTrace();
			}

			lastReceiveTime=System.currentTimeMillis();			
										
		}
				
	}
	
	public void encodeMessage(Message message) {
		if(isConnected()) {
			try {
				outputStream.writeByte(message.getType());
				message.write(outputStream);
			} catch (IOException e) {
				stopRequest=true;
				e.printStackTrace();
			}
		}
	}

	public long getLastReceiveTime() {
		return lastReceiveTime;
	}

}
