// LICENSE
package com.forgedui.model.titanium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class PlatformSupport {
	
	public static final Dimension iPhone1 = new Dimension(320, 480);
	public static final Dimension iPhone1_landscape = iPhone1.getTransposed();
	public static final Dimension iPhone2 = new Dimension(640, 960);
	public static final Dimension iPhone2_landscape = iPhone2.getTransposed();
	public static final Dimension iPhone5 = new Dimension(640,1136);
	public static final Dimension iPhone5_landscape = iPhone5.getTransposed();
	
	public static final Dimension iPad_portrait = new Dimension(768, 1024);
	public static final Dimension iPad_landscape = iPad_portrait.getTransposed();
	
	public static final Dimension window_iPhone1 = new Dimension(320, 480);
	public static final Dimension window_iPhone1_landscape = iPhone1.getTransposed();
	public static final Dimension window_iPhone2 = new Dimension(320, 480);
	public static final Dimension window_iPhone2_landscape = iPhone2.getTransposed();
	public static final Dimension window_iPhone5 = new Dimension(320, 568);
	public static final Dimension window_iPhone5_landscape = iPhone5.getTransposed();
	
	public static final Dimension window_iPad_portrait = new Dimension(768, 1024);
	public static final Dimension window_iPad_landscape = iPad_portrait.getTransposed();
	
	public static final Dimension android320_portrait = new Dimension(320, 480);//HVGA
	public static final Dimension android480_portrait1 = new Dimension(480, 800);//WVGA
	public static final Dimension android480_portrait2 = new Dimension(480, 854);//WVGA854
	
	public static final Dimension android320_landscape = android320_portrait.getTransposed();
	public static final Dimension android480_landscape1 = android480_portrait1.getTransposed();
	public static final Dimension android480_landscape2 = android480_portrait2.getTransposed();
	
	public static final Dimension BASE = iPhone1;
	
	private static Map<Platform, List<Dimension>> resolutions = new HashMap<Platform, List<Dimension>>();
	private static Map<Platform, List<Dimension>> window_resolutions = new HashMap<Platform, List<Dimension>>();
	
	static {
		List<Dimension> dimensions = new ArrayList<Dimension>();
		dimensions.add(iPhone1);
		dimensions.add(iPhone1_landscape);
		dimensions.add(iPhone2);
		dimensions.add(iPhone2_landscape);
		dimensions.add(iPhone5);
		dimensions.add(iPhone5_landscape);
		resolutions.put(Platform.iPhone, dimensions);
		
		dimensions = new ArrayList<Dimension>();
		dimensions.add(window_iPhone1);
		dimensions.add(window_iPhone1_landscape);
		dimensions.add(window_iPhone2);
		dimensions.add(window_iPhone2_landscape);
		dimensions.add(window_iPhone5);
		dimensions.add(window_iPhone5_landscape);
		window_resolutions.put(Platform.iPhone, dimensions);
		
		dimensions = new ArrayList<Dimension>();
		dimensions.add(iPad_portrait);
		dimensions.add(iPad_landscape);
		resolutions.put(Platform.iPad, dimensions);
		
		dimensions = new ArrayList<Dimension>();
		dimensions.add(window_iPad_portrait);
		dimensions.add(window_iPad_landscape);
		window_resolutions.put(Platform.iPad, dimensions);
		
		dimensions = new ArrayList<Dimension>();
		dimensions.add(android320_portrait);
		dimensions.add(android320_landscape);
		dimensions.add(android480_portrait1);
		dimensions.add(android480_portrait2);
		dimensions.add(android480_landscape1);
		dimensions.add(android480_landscape2);
		resolutions.put(Platform.Android, dimensions);
	}
	
	private Platform platform;
	
	private Dimension resolution;
	
	public PlatformSupport(Platform platform, Dimension resolution){
		this.platform = platform;
		this.resolution = resolution;
	}
	
	public Dimension getResolution(){
		return resolution.getCopy();
	}
	
	public void setResolution(Dimension resolution){
		this.resolution = resolution;
	}

	public Dimension viewToModel(Dimension def){
		return viewToModel(def, platform, resolution);
	}
	
	public Point viewToModel(Point def){
		return viewToModel(def, platform, resolution);
	}
	
	public Rectangle viewToModel(Rectangle def){
		return viewToModel(def, platform, resolution);
	}
	
	public int viewToModel(int x){
		return viewToModel(x, platform, resolution);
	}

	public Dimension modelToView(Dimension def){
		return modelToView(def, platform, resolution);
	}
	
	public Point modelToView(Point def){
		return modelToView(def, platform, resolution);
	}
	
	public Rectangle modelToView(Rectangle def){
		return modelToView(def, platform, resolution);
	}
	
	public int modelToView(int x){
		return modelToView(x, platform, resolution);
	}

	public static List<Dimension> getResolutions(Platform platform){
		return resolutions.get(platform);
	}
	
	public static List<Dimension> getWindowResolutions(Platform platform){
		return window_resolutions.get(platform);
	}
	
	public static Dimension getBaseResolution(Platform platform){
		return /*resolutions.get(platform).get(0)*/iPhone1;
	}
	
	private static double getWidthScale(Platform p, Dimension resolution){
		//Dimension baseResolution = PlatformSupport.getBaseResolution(p);
		//return 1.0*resolution.width / baseResolution.width;
		/*if (!(getBaseResolution(p).equals(resolution)
				|| getBaseResolution(p).getTransposed().equals(resolution))){
			return 2;
		}*/
		return 1;
	}
	
	/*private static double getHeightScale(Platform p, Dimension resolution){
		Dimension baseResolution = PlatformSupport.getBaseResolution(p);
		return 1.0*resolution.height / baseResolution.height;
	}*/
	
	private static Dimension viewToModel(Dimension def, Platform p, Dimension r){
		return def.scale(getWidthScale(p, r), getWidthScale(p, r));
	}
	
	private static Point viewToModel(Point def, Platform p, Dimension r){
		return def.scale(getWidthScale(p, r), getWidthScale(p, r));
	}
	
	private static Rectangle viewToModel(Rectangle def, Platform p, Dimension r){
		return def.scale(getWidthScale(p, r), getWidthScale(p, r));
	}
	
	private static int viewToModel(int x, Platform p, Dimension r){
		return (int) Math.floor(x * getWidthScale(p, r));
	}
	
	private static Dimension modelToView(Dimension def, Platform p, Dimension r){
		return def.scale(1.0/getWidthScale(p, r), 1.0/getWidthScale(p, r));
	}
	
	private static Point modelToView(Point def, Platform p, Dimension r){
		return def.scale(1.0/getWidthScale(p, r), 1.0/getWidthScale(p, r));
	}
	
	private static Rectangle modelToView(Rectangle def, Platform p, Dimension r){
		return def.scale(1.0/getWidthScale(p, r), 1.0/getWidthScale(p, r));
	}
	
	private static int modelToView(int x, Platform p, Dimension r){
		return (int) Math.floor(x * 1.0/getWidthScale(p, r));
	}

}
