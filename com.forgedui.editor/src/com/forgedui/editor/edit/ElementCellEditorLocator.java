package com.forgedui.editor.edit;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

/**
 * An element cell editor.
 * 
 * @author Tareq Doufish
 *
 */
public class ElementCellEditorLocator implements CellEditorLocator {

	private int HEIGHT = 20;
	
	private IFigure figure;
	
	/** We might want to deal with bounds instead of the svg figure here */
	private Rectangle bounds;

	public ElementCellEditorLocator(IFigure figure) {
		this.figure = figure;
	}
	
	public ElementCellEditorLocator(IFigure figure, Rectangle bounds) {
		this(figure);
		this.bounds = bounds;
	}
	
	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();
		Rectangle rect = null;
		if(bounds == null) { 
			rect = figure.getBounds().getCopy();
		} else { 
			rect = bounds.getCopy();
		}
		figure.translateToAbsolute(rect);
		org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
		rect.translate(trim.x, trim.y);
		rect.width += trim.width;
		rect.height = HEIGHT;
		if (text.getFont() != null){
			rect.height = FigureUtilities.getStringExtents("T", text.getFont()).height + 4;
		}
		text.setBounds(rect.x, rect.y - rect.height, rect.width, rect.height);
	}
}
