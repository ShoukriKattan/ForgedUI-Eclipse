package com.forgedui.editor.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

import com.forgedui.editor.palette.TitaniumUIElementFactory;
import com.forgedui.model.Diagram;
import com.forgedui.model.Element;

public class MyTemplateTransferDropTargetListener extends
		TemplateTransferDropTargetListener {
	
	private Diagram diagram;

	public MyTemplateTransferDropTargetListener(EditPartViewer viewer, Diagram diagram) {
		super(viewer);
		this.diagram = diagram;
	}

	@Override
	protected CreationFactory getFactory(Object template) {
		if (template instanceof Element) {
			//how this happened???
			return new TitaniumUIElementFactory(
					(Class<? extends Element>) template.getClass(), diagram);
			
		} else if (template instanceof Class){
			if (Element.class.isAssignableFrom((Class<?>) template)){
				return new TitaniumUIElementFactory(
						(Class<? extends Element>) template, diagram);
			}
			
		}
		throw new IllegalArgumentException("Can't create factory for " + template);
	}

}
