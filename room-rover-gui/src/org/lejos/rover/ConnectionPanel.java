package org.lejos.rover;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import lejos.pc.comm.NXTInfo;

import org.lejos.rover.remote.RemoteListener;
import org.lejos.rover.remote.RoverRemote;
import org.lejos.rover.ui.ColorIndicator;

public class ConnectionPanel extends JPanel implements ActionListener, RemoteListener, Runnable {
	private static final long serialVersionUID = 1L;

	private ColorIndicator bluetoothIndicator;
	private ColorIndicator availabilityIndicator;
	private ColorIndicator connectionIndicator;

	private JButton connectButton;

	private JComboBox roverCombo;

	private JButton disconnectButton;

	private JButton searchButton;

	public ConnectionPanel() {
		super(new FlowLayout(FlowLayout.LEFT));
		setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
		setBorder(BorderFactory.createTitledBorder("Connection"));

		bluetoothIndicator = new ColorIndicator();
		bluetoothIndicator.setColor(Color.black);
		add(bluetoothIndicator);

		availabilityIndicator = new ColorIndicator();
		availabilityIndicator.setColor(Color.black);
		add(availabilityIndicator);

		connectionIndicator = new ColorIndicator();
		connectionIndicator.setColor(Color.black);
		add(connectionIndicator);
		
		roverCombo = new JComboBox();
		roverCombo.setEnabled(false);
		roverCombo.addActionListener(this);
		roverCombo.setPreferredSize(new Dimension(200,(int)roverCombo.getPreferredSize().getHeight()));
		add(roverCombo);

		searchButton = new JButton("Search");
		searchButton.setEnabled(false);
		searchButton.setEnabled(false);
		searchButton.addActionListener(this);
		add(searchButton);
		
		connectButton = new JButton("Connect");
		connectButton.setEnabled(false);
		connectButton.addActionListener(this);
		add(connectButton);
		
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setEnabled(false);
		disconnectButton.addActionListener(this);
		add(disconnectButton);
		
		
		RoomRoverGui.getInstance().getRemote().addRemoteListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==searchButton) {
			//connectButton.setEnabled(false);
			//searchButton.setEnabled(false);
			//roverCombo.removeAllItems();
			RoomRoverGui.getInstance().getRemote().search();
		}
		if(e.getSource()==connectButton) {			
			RoomRoverGui.getInstance().getRemote().setTargetRover(((RoverItem)roverCombo.getSelectedItem()).getRover());
			Thread connectThread=new Thread(new Runnable() {				
				@Override
				public void run() {
					RoomRoverGui.getInstance().getRemote().connect();
				}
			});
			connectThread.start();
		}
		if(e.getSource()==disconnectButton) {
			RoomRoverGui.getInstance().getRemote().disconnect();
		}
		if(e.getSource()==roverCombo) {
			if(roverCombo.getSelectedItem()!=null) {
				connectButton.setEnabled(true);
			}
		}
	}

	@Override
	public void bluetoothInitialized(RoverRemote remote) {
		bluetoothIndicator.setColor(Color.green);		
	}
	
	@Override
	public void bluetoothFailed(RoverRemote remote) {
		bluetoothIndicator.setColor(Color.red);		
	}
	
	@Override
	public void roverConnectCompleted(RoverRemote remote) {
		connectionIndicator.setColor(Color.green);
		disconnectButton.setEnabled(true);
	}

	@Override
	public void roverDisconnected(RoverRemote remote) {
		connectionIndicator.setColor(Color.black);
		if(roverCombo.getSelectedItem()!=null) {
			connectButton.setEnabled(true);
		}
		disconnectButton.setEnabled(false);
		searchButton.setEnabled(true);
		if(roverCombo.getItemCount()>0) {
			roverCombo.setEnabled(true);
		}
	}
	
	@Override
	public void searchStarted(RoverRemote remote) {
		availabilityIndicator.setColor(Color.yellow);
		connectButton.setEnabled(false);
		searchButton.setEnabled(false);
		SwingUtilities.invokeLater(this);
	}

	@Override
	public void searchCompleted(RoverRemote remote) {
		if(remote.getAvailableRovers().length>0) {
			availabilityIndicator.setColor(Color.green);
		}
		else {
			availabilityIndicator.setColor(Color.blue);			
		}
		SwingUtilities.invokeLater(this);
		searchButton.setEnabled(true);
	}

	@Override
	public void searchFailed(RoverRemote roverRemote) {
		availabilityIndicator.setColor(Color.red);			
		roverCombo.setEnabled(false);
		searchButton.setEnabled(true);
	}

	@Override
	public void run() {
		NXTInfo[] rovers=RoomRoverGui.getInstance().getRemote().getAvailableRovers();
		roverCombo.removeAllItems();
		if(rovers!=null) {
			for(NXTInfo rover : rovers) {
				roverCombo.addItem(new RoverItem(rover));
			}
		}
		if(rovers!=null&&rovers.length>0) {
			roverCombo.setEnabled(true);
		} else {
			roverCombo.setEnabled(false);
		}
	}

	@Override
	public void roverConnectFailed(RoverRemote remote) {
		connectionIndicator.setColor(Color.red);
		if(roverCombo.getSelectedItem()!=null) {
			connectButton.setEnabled(true);
		}
		searchButton.setEnabled(true);
		if(roverCombo.getItemCount()>0) {
			roverCombo.setEnabled(true);
		}
	}

	@Override
	public void roverConnectStarted(RoverRemote remote) {
		connectionIndicator.setColor(Color.yellow);
		connectButton.setEnabled(false);
		searchButton.setEnabled(false);
		roverCombo.setEnabled(false);
	}
	
}
