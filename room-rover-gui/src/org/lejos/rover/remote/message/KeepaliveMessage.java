package org.lejos.rover.remote.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class KeepaliveMessage extends Message {

	public static int TYPE=1;
	
	public KeepaliveMessage() {
		super(TYPE);
	}

	@Override
	public void read(DataInputStream inputStream) throws IOException {
	}

	@Override
	public void write(DataOutputStream outputStream) throws IOException {
	}

}
