package org.lejos.rover;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public ControlPanel() {
		BorderLayout layout=new BorderLayout();
		this.setLayout(layout);
		//JToolBar toolBar=new JToolBar("Control Panel Buttons");
		//JButton testButton=new JButton("Test");
		//toolBar.add(testButton);
		//this.add(toolBar,BorderLayout.PAGE_START);
		JPanel panel=new JPanel();
		BoxLayout panelLayout=new BoxLayout(panel,BoxLayout.PAGE_AXIS);
		panel.setLayout(panelLayout);
		this.add(panel,BorderLayout.CENTER);
		panel.add(new ConnectionPanel());
	}
	
}
