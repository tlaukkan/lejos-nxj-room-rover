package org.lejos.rover.mapper;

import javax.swing.JFrame;

import org.lejos.rover.math.Vector2f;
import org.lejos.rover.ui.ImageCanvas;

public class RoomMapperVisualTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		JFrame frame=new JFrame("Room Mapper Visual Test");
		frame.setLocationByPlatform(true);
		frame.setSize(500, 500);
		
		RoomMapper mapper=new RoomMapper(5f,0.01f);

//		mapper.addConeObservation(new ObservationCone(new Vector2f(0f,0f),(float)Math.toRadians(0),(float)Math.toRadians(15),1f,true));
//		mapper.addConeObservation(new ObservationCone(new Vector2f(1f,0f),(float)Math.toRadians(0),(float)Math.toRadians(15),1f,true));
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0f,0f),(float)Math.toRadians(90),(float)Math.toRadians(15),1f,true));
//		mapper.addConeObservation(new ObservationCone(new Vector2f(1f,0f),(float)Math.toRadians(90),(float)Math.toRadians(15),1f,true));

//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(0),(float)Math.toRadians(15),1f,false));
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.1f,0f),(float)Math.toRadians(10),(float)Math.toRadians(15),1f,false));
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.2f,0f),(float)Math.toRadians(20),(float)Math.toRadians(15),1f,false));
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.3f,0f),(float)Math.toRadians(30),(float)Math.toRadians(15),1f,false));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.4f,0f),(float)Math.toRadians(40),(float)Math.toRadians(15),1f,false));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.5f,0f),(float)Math.toRadians(50),(float)Math.toRadians(15),1f,true));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.6f,0f),(float)Math.toRadians(60),(float)Math.toRadians(15),1f,true));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.7f,0f),(float)Math.toRadians(70),(float)Math.toRadians(15),1f,true));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.8f,0f),(float)Math.toRadians(80),(float)Math.toRadians(15),1f,true));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.9f,0f),(float)Math.toRadians(90),(float)Math.toRadians(15),1f,true));		
		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-0),(float)Math.toRadians(15),1f,false));
		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-1),(float)Math.toRadians(15),1f,false));
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-20),(float)Math.toRadians(15),1f,false));
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-30),(float)Math.toRadians(15),1f,false));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-40),(float)Math.toRadians(15),1f,false));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-50),(float)Math.toRadians(15),1f,true));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-60),(float)Math.toRadians(15),1f,true));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-70),(float)Math.toRadians(15),1f,true));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-80),(float)Math.toRadians(15),1f,true));		
//		mapper.addConeObservation(new ObservationCone(new Vector2f(0.0f,0f),(float)Math.toRadians(-90),(float)Math.toRadians(15),1f,true));		

		
		ImageCanvas canvas=new ImageCanvas(mapper.getImage());		
		frame.add(canvas);

		frame.setVisible(true);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		frame.setVisible(false);
		System.exit(0);
		
	}

}
