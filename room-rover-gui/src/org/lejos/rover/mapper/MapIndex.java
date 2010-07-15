package org.lejos.rover.mapper;

public class MapIndex {

	private int i;
	private int j;
	
	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}
	
	public MapIndex(int i, int j) {
		super();
		this.i = i;
		this.j = j;
	}

	@Override
	public boolean equals(Object obj) {
		return obj.getClass()==MapIndex.class&&((MapIndex)obj).i==i&&((MapIndex)obj).j==j;
	}

	@Override
	public int hashCode() {
		return new Integer(i).hashCode()+new Integer(j).hashCode();
	}

	@Override
	public String toString() {
		return "["+i+","+j+"]";

	}

}
