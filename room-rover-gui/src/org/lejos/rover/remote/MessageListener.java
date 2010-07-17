package org.lejos.rover.remote;

import org.lejos.rover.remote.message.Message;

public interface MessageListener {
	public void messageReceived(Message message);
}
