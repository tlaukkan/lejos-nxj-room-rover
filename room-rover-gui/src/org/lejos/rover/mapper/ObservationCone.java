package org.lejos.rover.mapper;

import org.lejos.rover.math.Vector2f;

public class ObservationCone {
	private Vector2f position;
	private float orientationAngle; 
	private float openingAngle;
	private float length;
	private float lengthAccuracy=0.10f;
	private boolean hit;
	
	public ObservationCone(Vector2f position, float orientationAngle, float openingAngle,
			float length, boolean hit) {
		super();
		this.position = position;
		this.orientationAngle = orientationAngle;
		this.openingAngle = openingAngle;
		this.length = length;
		this.hit=hit;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public float getOrientationAngle() {
		return orientationAngle;
	}

	public void setOrientationAngle(float orientationAngle) {
		this.orientationAngle = orientationAngle;
	}

	public float getOpeningAngle() {
		return openingAngle;
	}

	public void setOpeningAngle(float openingAngle) {
		this.openingAngle = openingAngle;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public boolean isInside(Vector2f vector) {
		Vector2f relative=vector.sub(position);
		return relative.length()<=length&&angleCloserThan(orientationAngle,relative.angle(),openingAngle);
	}
	
	private boolean angleCloserThan(float angle1, float angle2, float maxDelta) {
		float normalizedAngle1=angle1%(2*((float)Math.PI));
		float normalizedAngle2=angle2%(2*((float)Math.PI));
		if(Math.abs(normalizedAngle1-normalizedAngle2)<maxDelta) {
			return true;
		}
		if(normalizedAngle1<0) {
			normalizedAngle1+=2*((float)Math.PI);
		}
		if(Math.abs(normalizedAngle1-normalizedAngle2)<maxDelta) {
			return true;
		}
		else {
			if(normalizedAngle2<0) {
				normalizedAngle2+=2*((float)Math.PI);
			}
			return Math.abs(normalizedAngle1-normalizedAngle2)<maxDelta;
		}
	}
	
	public float getDeltaDistance(Vector2f vector) {
		return vector.sub(position).length();
	}
	
	public float getDeltaAngle(Vector2f vector) {
		float angle=vector.sub(position).angle();
		float normalizedAngle1=orientationAngle%(2*((float)Math.PI));
		float normalizedAngle2=angle%(2*((float)Math.PI));
		float delta=Math.abs(normalizedAngle1-normalizedAngle2);
		if(delta<openingAngle) {
			return delta;
		}
		if(normalizedAngle1<0) {
			normalizedAngle1+=2*((float)Math.PI);
		}
		delta=Math.abs(normalizedAngle1-normalizedAngle2);
		if(delta<openingAngle) {
			return delta;
		}
		else {
			if(normalizedAngle2<0) {
				normalizedAngle2+=2*((float)Math.PI);
			}
			delta=Math.abs(normalizedAngle1-normalizedAngle2);
			return delta;
		}
	}
	
	public float getProbability(Vector2f vector) {
		float deltaDistance=getDeltaDistance(vector);
		float deltaAngle=getDeltaAngle(vector);
		
		float angularProbability=(float)Math.cos(Math.PI/2*deltaAngle/openingAngle);
		float distanceProbability=0;
		
		float distanceToConeEnd=this.length-deltaDistance;
		if(distanceToConeEnd<lengthAccuracy&&hit) {
			distanceProbability=(float)Math.cos(Math.PI/2*distanceToConeEnd/lengthAccuracy);
		}
		
		return 0.5f+(angularProbability*(distanceProbability-0.5f));
	}
	
}
