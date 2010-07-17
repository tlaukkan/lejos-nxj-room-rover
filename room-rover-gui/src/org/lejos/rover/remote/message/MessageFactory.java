package org.lejos.rover.remote.message;

public class MessageFactory {
	public static Message constructMessage(int type) {
		if(type==KeepaliveMessage.TYPE) {
			return new KeepaliveMessage();
		}
		if(type==RadarPingMessage.TYPE) {
			return new RadarPingMessage();
		}
		return null;
	}
}
