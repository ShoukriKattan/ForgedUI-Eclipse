// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.swt.graphics.Color;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SmartRectangleFigure extends RectangleFigure {
	
	private boolean useLocalCoordinates;
	
	public SmartRectangleFigure(boolean useLocalCoordinates, Color borderColor, int borderWidth){
		this.useLocalCoordinates = useLocalCoordinates;
		setBorder(new LineBorder(borderColor, borderWidth));
	}
	
	@Override
	protected boolean useLocalCoordinates() {
		return this.useLocalCoordinates;
	}

}
