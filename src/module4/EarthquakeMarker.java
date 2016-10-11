package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Sean Thamer
 *
 */
public abstract class EarthquakeMarker extends SimplePointMarker
{
	
	// Did the earthquake occur on land?  This will be set by the subclasses.
	protected boolean isOnLand;

	// SimplePointMarker has a field "radius" which is inherited
	// by Earthquake marker:
	// protected float radius;
	//
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude. 
  
	
	
	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	// ADD constants for colors
	public static final int[][] DEPTH_COL = {{66, 170, 244},{244, 244, 66},{244, 80, 66},{244, 66, 232}};
	
	// abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	
	// constructor
	public EarthquakeMarker (PointFeature feature) 
	{
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude );
		setProperties(properties);
		this.radius = 2.75f*getMagnitude();

	}
	

	// calls abstract method drawEarthquake and then checks age and draws X if needed
	public void draw(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
			
		// determine color of marker from depth
		colorDetermine(pg);
		
		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);
		
		
		// OPTIONAL TODO: draw X over marker if within past day
		if ( getAge().equals("Past Hour")) {
			pg.line(x-(getRadius()), y-(getRadius()), x+(getRadius()), y+(getRadius()));
			pg.line(x+(getRadius()), y-(getRadius()), x-(getRadius()), y+(getRadius()));
		}
		
		// reset to previous styling
		pg.popStyle();

		
	}
	
	// determine color of marker from depth
	// We suggest: Deep = red, intermediate = blue, shallow = yellow
	// But this is up to you, of course.
	// You might find the getters below helpful.
	private void colorDetermine(PGraphics pg) {
		//TODO: Implement this method
		float depth = getDepth();
		if ( depth <= THRESHOLD_INTERMEDIATE ){
			pg.fill(DEPTH_COL[0][0], DEPTH_COL[0][1], DEPTH_COL[0][2]);
		}
		else if ( depth > THRESHOLD_INTERMEDIATE && depth < THRESHOLD_DEEP ) {
			pg.fill(DEPTH_COL[1][0], DEPTH_COL[1][1], DEPTH_COL[1][2]);
		}
		else if ( depth >= THRESHOLD_DEEP ) {
			pg.fill(DEPTH_COL[2][0], DEPTH_COL[2][1], DEPTH_COL[2][2]);
		}
	}
	
	
	/*
	 * getters for earthquake properties
	 */
	
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());	
	}
	
	public String getTitle() {
		return (String) getProperty("title");	
		
	}
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public String getAge() {
		return (String) getProperty("age");
		
	}
	
	public boolean isOnLand()
	{
		return isOnLand;
	}
	
	
}
