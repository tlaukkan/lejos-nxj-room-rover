package org.lejos.rover.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;

public class Transmitter implements Runnable {
		
	private Thread thread;
	private NXTComm communicator;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private MessageCoder messageCoder;
	private NXTInfo nxt;
	private boolean disconnectRequest;
	private long lastReceiveTime;

	public void setStopRequest(boolean stopRequest) {
		this.disconnectRequest = stopRequest;
	}

	public Transmitter(NXTComm communicator, NXTInfo nxt) {
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
		messageCoder=new MessageCoder(inputStream,outputStream);
		
		thread=new Thread(this);
		thread.start();
		return true;
	}
	
	public void disconnect() {
		disconnectRequest=true;
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
		
		while(!disconnectRequest) {
			
			try {
				
				messageCoder.decodeMessage();

				lastReceiveTime=System.currentTimeMillis();			
				
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			}
						
		}
				
	}

	public void sendKeepAlive() {
		try {
			messageCoder.encodeKeepalive();
		} catch (IOException e) {
			e.printStackTrace();
			disconnectRequest=true;
		}
	}

	public long getLastReceiveTime() {
		return lastReceiveTime;
	}

	public MessageCoder getMessageCoder() {
		return messageCoder;
	}
	
}
