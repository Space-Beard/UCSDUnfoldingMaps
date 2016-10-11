package GUIModule;

import processing.core.PApplet;

public class MyDisplay extends PApplet {

	public void setup()
	{
		size(400, 400);
		background(50, 50, 50);		
	}
	
	public void draw()
	{
		fill(255, 255, 0);
		ellipse(200, 200, 390, 390);
		fill(0,0,0);
		ellipse(125, 150, 50, 70);
		ellipse(275, 150, 50, 70);
		arc(200, 280, 100, 100, 0, PI);
	}
}
