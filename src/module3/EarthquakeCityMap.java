package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Sean Thamer
 * Date: Oct, 2016
 * */

public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.atom";

	//The List you will populate with new SimplePointMarkers
	List<SimplePointMarker> markers = new ArrayList<SimplePointMarker>();
	
	public void setup() {
		size(1280, 720, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 250, 25, 1000, 675, new OpenStreetMap.OpenStreetMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    map.setZoomRange(2, 15);
	    map.setTweening(true);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    // Creates a SimplePointMarker for each PointFeature created from the RSS feed
	    int index = 0;
	    for (PointFeature quake : earthquakes){
	    	// Gets the magnitude value for the current quake
	    	// and assigns it to the "mag" variable. Used to colour the markers later
	    	Object magObj = quake.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	int colorInt = (int) map(mag, 2.5f, 8f, 10, 255);
	    	int minColor = color(66, 170, 244);
	    	int medColor = color(244, 244, 66);
	    	int majColor = color(244, 80, 66);
	    	int greatColor = color(244, 66, 232);

	    	
	    	// Adds a SimplePointMarker with the location of the current PointFeature
	    	markers.add(createMarker(quake));
	    	
	    	//Styles the markers depending on magnitude
	    	if(mag < 4.9f){
	    		markers.get(index).setColor(minColor);
	    	}
	    	else if(mag >= 4.9f && mag < 6f){
	    		markers.get(index).setColor(medColor);
	    	}
	    	else if(mag >= 6f && mag < 8f){
	    		markers.get(index).setColor(majColor);
	    	}
	    	else{
	    		markers.get(index).setColor(greatColor);
	    	}
	    	
	    	
	    	
	    	markers.get(index).setRadius((mag/2) * (mag/2) * 2);	    	
	    	map.addMarkers(markers.get(index));
	    	index++;
	    }
	    
	    
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		return new SimplePointMarker(feature.getLocation());
	}
	
	public void draw() {
		int index = 0;
	    background(75);
	    map.draw();
	    addKey();
	  
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		int minColor = color(66, 170, 244);
    	int medColor = color(244, 244, 66);
    	int majColor = color(244, 80, 66);
    	int greatColor = color(244, 66, 232);
		
		fill(255);
		rect(25, 25, 200, 400, 5);
		String title = "Magnitude Key";
		fill(50);
		textSize(20);
		text(title, 54, 26, 200, 300);
		fill(minColor);
		ellipse(40, 75, 5, 5);
		fill(50);
		textSize(14);
		text("Below 4.9: Light earthquakes", 60, 65, 140, 360);
		fill(medColor);
		ellipse(40, 125, 10, 10);
		fill(50);
		text("4.9-5.9: Moderate earthquakes", 60, 115, 140, 360);
		fill(majColor);
		ellipse(40, 175, 15, 15);
		fill(50);
		text("6-7.9: Strong/Major earthquakes", 60, 165, 140, 360);
		fill(greatColor);
		ellipse(40, 225, 20, 20);
		fill(50);
		text("8+: Great earthquakes", 60, 215, 140, 360);
		
	
	}
}
