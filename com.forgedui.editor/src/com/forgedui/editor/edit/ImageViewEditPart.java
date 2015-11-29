package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.io.File;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com.forgedui.editor.figures.ImageViewFigure;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.model.titanium.ImageView;
import com.forgedui.swt.dialogs.FileImageSource;
import com.forgedui.swt.dialogs.ISourceImage;
import com.forgedui.swt.dialogs.ImageBrowserDialog;
import com.forgedui.util.Converter;

/**
 * Image edit part.
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ImageViewEditPart extends TitaniumElementEditPart<ImageView> {
	
	protected void refreshVisuals() {
		ImageViewFigure figure = (ImageViewFigure)getFigure();
		ImageView model = getModel();
		figure.getBorder().setOpaque(getBackgroundColor() == null && getBackgroundImage() == null);
		figure.setImage(Converter.getImageFullPath(model.getDiagram(), model.getImage()));
		super.refreshVisuals();
	}

	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		ImageView model = (ImageView)getModel();
		if (model.getWidth() == null && model.getHeight() == null){
			ImageViewFigure figure = (ImageViewFigure)getFigure();
			if (figure.getImage() != null){
				bounds.setSize(figure.getImage().getBounds().width, figure.getImage().getBounds().height);
			} else {
				bounds.setSize(getDefaults().getSize(model));
			}
		}
		return bounds;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean isTypeVisiual = PropertyType.isVisualProperty(getModel().getClass().getName(), evt.getPropertyName());
		if (isTypeVisiual){
			refreshVisuals();
		} else {
			super.propertyChange(evt);
		}
	}
	
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_OPEN) {
			ISourceImage image = null;
			ImageView model = getModel();
	    	String fullImagePath = Converter.getImageFullPath(model.getDiagram(), model.getImage());
	    	String url = null;
	        // If we already have an image here.
	    	if (fullImagePath != null){
	    		if (fullImagePath.startsWith("http://")){
		    		url = fullImagePath;
		    	} else {
	        		image = new FileImageSource(new File(fullImagePath));
		        }
	    	}
	    	
			ImageBrowserDialog dialog = new ImageBrowserDialog(image, true, url);
			//dialog.create();
			int res = dialog.open();
	    	if (res == Window.OK) {
    			model.setImage(dialog.getSelectedImagePath());
    			//update properties view and don't block ui thread
    			Display.getCurrent().asyncExec(new Runnable(){
    				public void run() {
    					getViewer().deselect(ImageViewEditPart.this);
    					getViewer().select(ImageViewEditPart.this);
        			};
    			});
	    	}
		} else {
			super.performRequest(request);
		}
	}
}
