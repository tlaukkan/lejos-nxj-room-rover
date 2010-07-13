
public interface RadarListener {
	public void radarStarted();
	public void radarStopped();
	public void radarPinged(RadarPingEvent event);
}
