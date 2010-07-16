package org.lejos.rover.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;

public class Transmitter implements Runnable {
	
	private static byte KEEP_ALIVE=1;
	
	private Thread thread;
	private NXTComm communicator;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private NXTInfo nxt;
	private boolean disconnectRequest;

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
		
		long lastReceiveTime=System.currentTimeMillis();
		
		while(!disconnectRequest) {
			
			try {
				byte messageType=inputStream.readByte();
				
				// Checking for timeout
				long receiveTime=System.currentTimeMillis();
				
				if(receiveTime-lastReceiveTime>5000) {
					System.out.println("Receive timeout => disconnecting.");
					break;
				}
				
				lastReceiveTime=receiveTime;

				//TODO Read message from stream.
			
				
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			}
						
		}
				
	}

	public void sendKeepAlive() {
		try {
			outputStream.writeByte(KEEP_ALIVE);
		} catch (IOException e) {
			e.printStackTrace();
			disconnectRequest=true;
		}
	}
	
}
