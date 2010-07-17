package org.lejos.rover;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.lejos.rover.remote.RemoteLink;

public class RoomRoverGui implements Runnable, WindowListener{

	private static RoomRoverGui gui;
		
	public static RoomRoverGui getInstance() {
		return gui;
	}
	
	public static void main(String[] args) {
		gui=new RoomRoverGui();
		
        try {
            SwingUtilities.invokeAndWait(gui);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
	}
	
	private JFrame frame;
	private RemoteLink remoteLink;
	private EventBroker eventBroker;
	
	public RoomRoverGui()
	{
	}
	
	public void run() {
		eventBroker=new EventBroker();
		remoteLink=new RemoteLink();
		remoteLink.addRemoteListener(eventBroker);
		remoteLink.addMessageListener(eventBroker);
		
		frame=new JFrame("Room Rover GUI");
		
		frame.addWindowListener(this);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		//.setLocationByPlatform(true);
		frame.setSize(1000, 800);
		
		JTabbedPane tabPane=new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabPane);
		
		ControlPanel controlPanel=new ControlPanel();		
		tabPane.addTab("Control Panel",controlPanel);

		JPanel testPanel2=new JPanel();		
		tabPane.addTab("Test Panel",testPanel2);

		//frame.pack();
		frame.setVisible(true);	
		
		remoteLink.start();
	}
	
	public RemoteLink getRemoteLink() {
		return remoteLink;
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		frame.setVisible(false);
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
