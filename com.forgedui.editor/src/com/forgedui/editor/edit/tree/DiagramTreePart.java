// LICENSE
package com.forgedui.editor.edit.tree;

import java.util.ArrayList;
import java.util.List;

import com.forgedui.model.Diagram;
import com.forgedui.model.Element;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DiagramTreePart extends ContainerTreeEditPart {

	/**
	 * @param c
	 */
	public DiagramTreePart(Diagram d) {
		super(d);
	}
	
	public List<?> getModelChildren(){
		Diagram diagram = (Diagram)getModel();
		List<Element> children = new ArrayList<Element>(diagram.getChildren());
		children.add(diagram.getScreen());
		return children;
	}

}
