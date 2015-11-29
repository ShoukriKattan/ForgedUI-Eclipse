package com.forgedui.editor.edit.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.ElementImpl;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.TabbedBar;
import com.forgedui.model.titanium.TitaniumUIBaseElement;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class CloneCommand extends Command {

	private List<Element> parts, newTopLevelParts;
	private Container parent;
	private Map<Element, Rectangle> bounds;
	private Map<Element, Element> connectionPartMap;

	public CloneCommand() {
		super("Clone element(s)");
		parts = new LinkedList<Element>();
	}

	public void addElement(Element part, Rectangle newBounds) {
		parts.add(part);
		if (bounds == null) {
			bounds = new HashMap<Element, Rectangle>();
		}
		bounds.put(part, newBounds);
	}

	public void addElement(Element part) {
		parts.add(part);
	}

	protected void cloneElement(Element oldPart, Container newParent,
			Rectangle newBounds, Map<Element, Element> connectionPartMap) {
		Element newPart = null;
		
		if (oldPart instanceof TitaniumUIBaseElement){
			try {
				newPart = ((TitaniumUIBaseElement)oldPart).getCopy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalStateException("Cant clone this: " + oldPart);
		}
		
		newParent.addChild(newPart);

		if (oldPart instanceof Picker){
			((Picker)newPart).setColumnCount(0);//clean
		} else if (oldPart instanceof ButtonBar){
			((ButtonBar)newPart).setButtonsNumber(0);//clean
		} else if (oldPart instanceof TabbedBar){
			((TabbedBar)newPart).setButtonsNumber(0);//clean
		} else if (oldPart instanceof ButtonBar){
			((ButtonBar)newPart).setButtonsNumber(0);//clean
		} else if (oldPart instanceof Picker){
			((Picker)newPart).setColumnCount(0);//clean
		} else if (oldPart instanceof Container) {
			for (Element child : ((Container) oldPart).getChildren()) {
				// for children they will not need new bounds
				cloneElement(child, (Container) newPart,
						null, connectionPartMap);
			}
		}

		if (newPart instanceof TitaniumUIBoundedElement) {
			TitaniumUIBoundedElement titNewPart = (TitaniumUIBoundedElement) newPart;
			TitaniumUIBoundedElement titOldPart = (TitaniumUIBoundedElement) oldPart;
			titNewPart.setWidth(titOldPart.getWidth());
			titNewPart.setHeight(titOldPart.getHeight());
			titNewPart.setLeft(titOldPart.getLeft());
			titNewPart.setTop(titOldPart.getTop());
			titNewPart.setRight(titOldPart.getRight());
			titNewPart.setBottom(titOldPart.getBottom());
		} else if (newBounds != null){
			ElementImpl eNewPart = (ElementImpl)newPart;
			eNewPart.setDimension(newBounds.getSize());
			eNewPart.setLocation(newBounds.getLocation());
		}

		// keep track of the new parts so we can delete them in undo
		// keep track of the oldpart -> newpart map so that we can properly
		// attach
		// all connections.
		if (newParent == parent)
			newTopLevelParts.add(newPart);
		connectionPartMap.put(oldPart, newPart);
	}

	public void execute() {
		connectionPartMap = new HashMap<Element, Element>();
		newTopLevelParts = new LinkedList<Element>();

		Iterator<Element> i = parts.iterator();

		Element part = null;
		while (i.hasNext()) {
			part = (Element) i.next();
			if (bounds != null && bounds.containsKey(part)) {
				cloneElement(part, parent, (Rectangle) bounds.get(part),
						 connectionPartMap);
			} else {
				cloneElement(part, parent, null, connectionPartMap);
			}
		}
	}

	public void setParent(Container parent) {
		this.parent = parent;
	}

	public void redo() {
		//do nothing
	}


	public void undo() {
		for (Iterator<Element> iter = newTopLevelParts.iterator(); iter.hasNext();)
			parent.removeChild(iter.next());
	}

}
