/**
 * 
 */
package com.forgedui.model.titanium.properties;

/**
 * @author shoukry
 *
 */
public class Point {

	protected Float x;
	protected Float y;
	
	public void init(Point parent){
		if (parent != null && parent.x != null){
			x = new Float(parent.x);
		}
		if (parent != null && parent.y != null){
			x = new Float(parent.y);
		}
	}
}
