package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Parsing library
import parsing.ParseFeed;
//Processing library
import processing.core.PApplet;
import processing.core.PFont;
//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
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
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	private PFont f;
	
	public void setup() {
		size(950, 600, OPENGL);
		f= createFont("Futura", 14);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	//PointFeature f = earthquakes.get(0);
	    //	System.out.println(f.getProperties());
	    	// PointFeatures also have a getLocation method
	    	  for(PointFeature earthquake:earthquakes){
	  	    	SimplePointMarker marker = createMarker(earthquake);
	  	    	float mag = Float.parseFloat(earthquake.getProperty("magnitude").toString());
	  	    	if(mag >= THRESHOLD_MODERATE){
	  	    		marker.setColor(color(255, 0, 0));
	  	    		marker.setRadius(10);
	  	    		markers.add(marker);
	  	  	    	  	    	
	  	    	}else if(mag < THRESHOLD_LIGHT){
	  	    		marker.setColor(color(0, 0, 255));
	  	    		marker.setRadius(5);
	  	    		markers.add(marker);
	  	  	    	
	  	    	} else{
	  	    		marker.setColor(color(255, 255, 0));
	  	    		marker.setRadius(8);
	  	    		markers.add(marker);
	  	  	    	
	  	    	}
	  	    	}
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	  
	    //int yellow = color(255, 255, 0);
	    
	    //TODO: Add code here as appropriate
	
	    map.addMarkers(markers);
	    
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
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(color(255,255,255));
		rect(10, 50, 190, 250);
		
		
		textFont(f, 14);
		fill(0);
		text("Earthquake Key",15,70);
		
		addLableAndMarker(color(255,0,0), 20, 100, 10, 10, 14, 0, "5.0+ Magnitude", 50, 105);

		addLableAndMarker(color(255,255,0), 20, 130, 8, 8, 14, 0, "4.0+ Magnitude", 50, 135);

		addLableAndMarker(color(0,0,255), 20, 160, 5, 5, 14, 0, "Below 4.0", 50, 165);

	}
	
	private void addLableAndMarker(int ellipseColor,int ellipseX, int ellipseY,int ellipseW, int ellipseH, 
			int fontSx, int txtColor, String txtLbl, int txtX, int txtY	){

		fill(color(ellipseColor));
		ellipse(ellipseX, ellipseY, ellipseW, ellipseH);

		textFont(f, 14);
		fill(0);
		text(txtLbl,txtX,txtY);

	}
}
