package com.forgedui.util;

import org.eclipse.draw2d.geometry.Dimension;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.PlatformSupport;

/**
 * The screen manager is a class that will handle and return all the properties
 * of the screen at the editor. Because we have a special case with the platforms.
 * 
 * @author Tareq Doufish
 *
 */
public class ScreenManager {
	
	//view carrier height
	public static int CarrierHeight = 20;
	
	public static final int TITLE_BAR_BUTTONS_INSET = 6;
	
	public static final int TITLE_BAR_PROMPT_HEIGHT = 20;

	/**
	 * @return Rectangle in model pixels
	 */
	public static Dimension getScreenWindowBounds(Element e) {
		return getScreenWindowBounds(e.getSupport());
	}
	
	public static Dimension getScreenWindowBounds(PlatformSupport support) {
		Dimension res = support.getResolution();
		res.height -= support.viewToModel(ScreenManager.CarrierHeight);
		return res;
	}
	
	/**
	 * 
	 * @param e
	 * @return Model TabGroup Size
	 */
	public static Dimension getTabGroupSize(Element e){
		if (e.getPlatform() == Platform.iPhone) {
			return new Dimension(getScreenWindowBounds(e).width, e.getSupport().viewToModel(54));
		} else if (e.getPlatform() == Platform.iPad) {
			return new Dimension(getScreenWindowBounds(e).width, e.getSupport().viewToModel(97));
		} else if (e.getPlatform() == Platform.Android) {
			return new Dimension(getScreenWindowBounds(e).width, e.getSupport().viewToModel(54));
		}
		return null;
	}
	
	public static Dimension getToolBarSize(Element e){
		return new Dimension(getScreenWindowBounds(e).width, 43);
	}
	
}
