import lejos.nxt.*;

/**
 * This is a test for Ultrasonic Radar.
 * 
 * @author Tommi Laukkanen
 *
 */
public class RadarTest implements RadarListener {
	
	public void test()
	{
		Radar radar=new Radar(new UltrasonicSensor(SensorPort.S3), Motor.A);
		radar.setRadarSweepSpeed(10);
		radar.addRadarListener(this);
		
		radar.start();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
		
		radar.stop();		
	}
	
	public static void main(String[] args) {
		
		RadarTest test=new RadarTest();
		test.test();
		
	}

	@Override
	public void radarPinged(RadarPingEvent event) {
		System.out.println(""+
				" a = " + event.getAngle() +
				" d = " + event.getDistance()
			);
	}

	@Override
	public void radarStarted() {
		System.out.println("Radar start");
	}

	@Override
	public void radarStopped() {
		System.out.println("Radar stop");
	}
}
