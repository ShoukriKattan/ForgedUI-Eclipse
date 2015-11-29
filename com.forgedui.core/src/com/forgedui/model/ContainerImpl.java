// LICENSE
package com.forgedui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.forgedui.model.titanium.TitaniumUIBaseElement;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ContainerImpl extends ElementImpl implements Container{

	private static final long serialVersionUID = 2;

	protected List<Element> children;
	
	public ContainerImpl() {
		children = new ArrayList<Element>();
	}
	
	public void addChild(Element child) {
		addChild(child, -1);
	}

	public void removeChild(Element child) {
		if ( this.children.remove(child) ){
			child.setParent(null);
			listeners.firePropertyChange(PROPERTY_CHILDREN, child, null);
		}
	}
	
	public void addChild(Element child, int index) {
		child.setParent(this);
		if (index >= 0)
			children.add(index,child);
		else
			children.add(child);
		listeners.fireIndexedPropertyChange(PROPERTY_CHILDREN, index, null, child);
	}
	
	public void moveChild(Element child, int toIndex) {
		int currentIndex = getChildren().indexOf(child);
		if (currentIndex >= 0 && toIndex < getChildren().size()
				&& toIndex >=0 && toIndex != currentIndex){
			children.remove(currentIndex);
			if (currentIndex < toIndex){
				toIndex--;
			}
			children.add(toIndex, child);
			listeners.fireIndexedPropertyChange(PROPERTY_CHILDREN, toIndex, null, child);
		}
	}

	public List<Element> getChildren() {
		return Collections.unmodifiableList(children);
	}

}
