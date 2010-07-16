package org.lejos.rover;

import lejos.pc.comm.NXTInfo;

public class RoverItem {
	private NXTInfo rover;

	public RoverItem(NXTInfo rover) {
		super();
		this.rover = rover;
	}

	public NXTInfo getRover() {
		return rover;
	}

	@Override
	public String toString() {
		return rover.name;
	}
	
	
}
