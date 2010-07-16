package org.lejos.rover.ui;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ColorIndicator extends JPanel {
	private static final long serialVersionUID = 1L;

	public ColorIndicator() {
		super();
		setSize(30,30);
		LineBorder border=new LineBorder(Color.gray,3,true);
		setBorder(border);
	}
	
	public void setColor(Color color) {
		setBackground(color);
	}
	
}
