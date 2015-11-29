// LICENSE
package com.forgedui.model;

import java.util.List;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public interface Container extends Element {
	
	public final String PROPERTY_CHILDREN = "children";
	
	public void addChild(Element child);
	public void addChild(Element child, int index);
	public void moveChild(Element child, int toIndex);
	public void removeChild(Element child);
	public List<? extends Element> getChildren();
}
