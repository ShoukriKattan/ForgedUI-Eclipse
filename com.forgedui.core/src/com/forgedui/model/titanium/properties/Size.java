/**
 * 
 */
package com.forgedui.model.titanium.properties;

/**
 * @author shoukry
 * 
 *         the size of the view as a dictionary of width and height properties
 */
public class Size {

	protected Float height;
	protected Float width;
	
	public Size(int width, int height){
		this.width = (float)width;
		this.height = (float)height;
	}
	
	public Size(Float width, Float height){
		this.width = width;
		this.height = height;
	}
	
	public Size(Size size){
		this(size != null ? size.width : null,
				size != null ? size.height : null);
	}
	
	
	/**
	 * @return the height
	 */
	public Float getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(Float height) {
		this.height = height;
	}
	/**
	 * @return the width
	 */
	public Float getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(Float width) {
		this.width = width;
	}

}
