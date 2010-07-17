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

	private RemoteLink remote;
	private NXTComm communicator;
	private NXTInfo nxt;

	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	private Thread thread;

	private boolean stopRequest;
	private boolean connected=true;
	private long lastReceiveTime;

	public void setStopRequest(boolean stopRequest) {
		this.stopRequest = stopRequest;
	}

	public Transmitter(RemoteLink remote, NXTComm communicator, NXTInfo nxt) {
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
		return connected;
	}

	@Override
	public void run() {		
				
		while(!stopRequest) {
			
			try {
				
				int messageType = inputStream.readUnsignedByte();
				System.out.print("("+messageType+")");
				lastReceiveTime=System.currentTimeMillis();			
				
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
					//stopRequest=true; // Unknown message type. Probably transmission error.				e.printStackTrace();
				}
				
			} catch (IOException e) {
				stopRequest=true;
				e.printStackTrace();
			}
										
		}
			
		connected=false;
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
