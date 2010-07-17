package org.lejos.rover;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.lejos.rover.mapper.ObservationCone;
import org.lejos.rover.mapper.RoomMapper;
import org.lejos.rover.math.Vector2f;
import org.lejos.rover.remote.MessageListener;
import org.lejos.rover.remote.message.Message;
import org.lejos.rover.remote.message.RadarPingMessage;
import org.lejos.rover.ui.ImageCanvas;

public class MapperPanel extends JPanel implements MessageListener, Runnable {
	private static final long serialVersionUID = 1L;
	private ImageCanvas imageCanvas;
	private long lastRedrawTime=0;

	public MapperPanel() {
		super(new BorderLayout());
		setMaximumSize(new Dimension(Integer.MAX_VALUE,500));
		setBorder(BorderFactory.createTitledBorder("Room Mapping"));

		imageCanvas = new ImageCanvas(RoomRoverGui.getInstance().getRoomMapper().getImage());		
		add(imageCanvas,BorderLayout.CENTER);
		
		RoomRoverGui.getInstance().getRemoteLink().addMessageListener(this);
	}

	@Override
	public void messageReceived(Message message) {
		if(message.getType()==RadarPingMessage.TYPE) {
			RadarPingMessage ping=(RadarPingMessage) message;
			System.out.println("Radar ping x: "+ping.getX()+" y :"+ping.getY()+" a: "+ping.getAngle()+" d: "+ping.getDistance());
			RoomMapper mapper=RoomRoverGui.getInstance().getRoomMapper();
			mapper.addConeObservation(
					new ObservationCone(new Vector2f(ping.getX(),ping.getY()),(float)Math.toRadians(ping.getAngle()),(float)Math.toRadians(7),ping.getDistance()/100.0f,ping.getDistance()!=255)
			);
			imageCanvas.setImage(mapper.getImage());
			if(System.currentTimeMillis()-lastRedrawTime>200) {
				SwingUtilities.invokeLater(this);
				lastRedrawTime=System.currentTimeMillis();
			}
		}
	}

	@Override
	public void run() {
		imageCanvas.repaint();
	}
}
