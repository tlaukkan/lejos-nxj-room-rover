package org.lejos.rover.math;

/**
 * Simple 2D float vector implementation.
 * @author Tommi Laukkanen
 */
public class Vector2f {
	private float x;
	private float y;
	
	public Vector2f(float x, float y) {
		this.x=x;
		this.y=y;
	}
	
	public static Vector2f fromPolar(float angle, float length) {
		return new Vector2f(
				(float)(Math.cos(angle)*length),
				(float)(Math.sin(angle)*length));
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
		
	public float angle() {
		return (float) Math.atan2(y, x);
	}
	
	public float length() {
		return (float) Math.sqrt(x*x+y*y);
	}
	
	public Vector2f add(Vector2f vector) {
		return new Vector2f(x+vector.x,y+vector.y);
	}
	
	public Vector2f sub(Vector2f vector) {
		return new Vector2f(x-vector.x,y-vector.y);
	}

	@Override
	public boolean equals(Object obj) {
		return obj.getClass()==Vector2f.class&&((Vector2f)obj).x==x&&((Vector2f)obj).y==y;
	}
	
	@Override
	public int hashCode() {
		return new Float(x).hashCode()+new Float(y).hashCode();
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}

}
