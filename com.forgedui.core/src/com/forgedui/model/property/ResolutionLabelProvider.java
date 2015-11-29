// LICENSE
package com.forgedui.model.property;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ResolutionLabelProvider extends LabelProvider {
	
	@Override
	public String getText(Object element) {
		if (element instanceof Dimension) {
			Dimension dim = (Dimension) element;
			return dim.width + "x" + dim.height;
		}
		return super.getText(element);
	}

}
