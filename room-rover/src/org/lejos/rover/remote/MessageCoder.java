package org.lejos.rover.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class MessageCoder {
	
	private static int KEEPALIVE=1;
	private static int RADAR_PING=20;
	
	private boolean connected=true;
	private boolean exception=false;
	
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private ArrayList listeners=new ArrayList();
	
	public MessageCoder(DataInputStream inputStream,
			DataOutputStream outputStream) {
		super();
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}
	
	public void disconnect() {
		synchronized(this) {
			connected=false;
		}
	}
	
	public void addMessageListener(MessageListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	public void removeMessageListener(MessageListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}

	
	public void decodeMessage() throws IOException {
		if(!exception) {

			try {
				
				int messageType=inputStream.readUnsignedByte();
				
				if(messageType==KEEPALIVE) {
					synchronized(listeners) {
						for(Object listener :  listeners) {
							((MessageListener)listener).keepalive();
						}
					}
				}
		
				if(messageType==RADAR_PING) {
					synchronized(listeners) {
						for(Object listener :  listeners) {
							((MessageListener)listener).radarPing(
									inputStream.readInt(),
									inputStream.readInt(),
									inputStream.readInt(),
									inputStream.readInt()							
							);
						}
					}
				}
			
			}
			catch(IOException e) {
				exception=true;
			}
		
		}
		else {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}

	}
	
	public void encodeKeepalive() {
		synchronized(this) {
			if(!exception) {
				try {
					outputStream.writeByte(KEEPALIVE);
					outputStream.flush();
				}
				catch(IOException e) {
					exception=true;
				}
			}
		}
	}
	
	public void encodeRadarPing(int x, int y, int angle, int distance) {
		synchronized(this) {
			if(!exception) {
				try {
					outputStream.writeByte(RADAR_PING);
					outputStream.flush();
					outputStream.writeInt(x);
					outputStream.flush();
					outputStream.writeInt(y);
					outputStream.flush();
					outputStream.writeInt(angle);
					outputStream.flush();
					outputStream.writeInt(distance);
					outputStream.flush();
				}
				catch(IOException e) {
					exception=true;
				}
			}
		}
	}

	public boolean isException() {
		return exception;
	}

	public boolean isConnected() {
		return connected;
	}

		
}
