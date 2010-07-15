package org.lejos.rover.ui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

public class ImageCanvas extends Canvas {
	private static final long serialVersionUID = 1L;

	private Image image;
	
	public ImageCanvas(Image image) {
		super();
		
		this.image=image;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(image,0,0,null);
	}
	
	


}
