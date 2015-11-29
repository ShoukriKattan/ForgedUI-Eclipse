/**
 * 
 */
package com.forgedui.model.titanium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @author shoukry
 * 
 *         TODO: I chose to extend container and copy paste the properties that
 *         i need
 * 
 * 
 */
public class TitaniumUIContainer extends TitaniumUIElement implements Container {

	@Unmapped
	private static final long serialVersionUID = 1L;

	@Unmapped
	protected List<TitaniumUIBaseElement> children;

	@Unmapped
	public final static String PROPERTY_CHILDREN = "children";

	public TitaniumUIContainer() {
		children = new ArrayList<TitaniumUIBaseElement>();
	}

	public void addChild(Element child) {
		addChild(child, -1);
	}
	
	
	public void removeChild(Element child) {
		this.children.remove(child);
		if (child != null) {
			child.setParent(null);
		}
		listeners.firePropertyChange(PROPERTY_CHILDREN, child, null);
	}
	
	public void addChild(Element child, int index) {
		if (index >= 0)
			children.add(index, (TitaniumUIBaseElement) child);
		else
			children.add((TitaniumUIBaseElement) child);
		if (child != null) {
			child.setParent(this);
		}
		listeners.fireIndexedPropertyChange(PROPERTY_CHILDREN, index, null,
				child);
	}
	
	public void moveChild(Element child, int toIndex) {
		int currentIndex = getChildren().indexOf(child);
		if (currentIndex >= 0 && toIndex < getChildren().size()
				&& toIndex >=0 && toIndex != currentIndex){
			children.remove(currentIndex);
			if (currentIndex < toIndex){
				toIndex--;
			}
			children.add(toIndex, (TitaniumUIBaseElement) child);
			listeners.fireIndexedPropertyChange(PROPERTY_CHILDREN, toIndex, null, child);
		}
	}

	public List<? extends TitaniumUIBaseElement> getChildren() {
		return Collections.unmodifiableList(children);
	}
}
