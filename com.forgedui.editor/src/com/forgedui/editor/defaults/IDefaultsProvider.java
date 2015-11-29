// LICENSE
package com.forgedui.editor.defaults;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.PlatformSupport;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public interface IDefaultsProvider {
	
	Dimension getSize(Element e);
	
	Dimension getSize(Element e, PlatformSupport support);
	
	Font getFont(Element e);
	
	Color getForegroundColor(Element e);
	
	Color getBackgroundColor(Element e);
	
	Color getBorderColor(Element e);
	
	Color getAdditionColor(Element e);
	
	float getBorderRadius(Element e);

	float getBorderWidth(Element e);

	boolean getShowGradient(Element e);
}
