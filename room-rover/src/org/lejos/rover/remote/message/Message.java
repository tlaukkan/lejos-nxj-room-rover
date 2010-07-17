package org.lejos.rover.remote.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Message {

	private int type;

	public Message(int type) {
		super();
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public abstract void read(DataInputStream inputStream) throws IOException;
	
	public abstract void write(DataOutputStream outputStream) throws IOException;
		
}
