// LICENSE
package com.forgedui.editor;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.swt.graphics.Color;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class GridScalableFreeformRootEditPart extends
		ScalableFreeformRootEditPart {
	
	/**
	 * Creates a layered pane and the layers that should be scaled.
	 * 
	 * @return a new freeform layered pane containing the scalable layers
	 */
	protected ScalableFreeformLayeredPane createScaledLayers() {
		ScalableFreeformLayeredPane layers = new ScalableFreeformLayeredPane();
		layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
		layers.add(new FeedbackLayer(), SCALED_FEEDBACK_LAYER);
		layers.add(createGridLayer(), GRID_LAYER);
		return layers;
	}
	
	protected GridLayer createGridLayer() {
		GridLayer gl = new GridLayer();
		gl.setForegroundColor( new Color(null, 180, 180, 255));
		return gl;
	}
	
	class FeedbackLayer extends FreeformLayer {
		FeedbackLayer() {
			setEnabled(false);
		}
	}

}
