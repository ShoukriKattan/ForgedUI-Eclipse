package com.forgedui.editor.edit;

import org.eclipse.draw2d.IFigure;

import com.forgedui.editor.figures.ViewFigure;
import com.forgedui.model.titanium.View;

/**
 * The view edit part of the editor.
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ViewEditPart extends TitaniumContainerEditPart<View> {


	protected IFigure createFigure() {
		ViewFigure figure = (ViewFigure) super.createFigure();
		figure.setOpaque(true);
		return figure;
	}

	
}

