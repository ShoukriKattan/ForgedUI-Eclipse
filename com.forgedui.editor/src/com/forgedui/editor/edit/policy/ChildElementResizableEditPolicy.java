package com.forgedui.editor.edit.policy;

import java.util.Iterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.forgedui.editor.edit.ElementEditPart;
import com.forgedui.editor.edit.ScrollViewEditPart;
import com.forgedui.editor.edit.WindowEditPart;
import com.forgedui.editor.figures.ScrollViewFigure;
import com.forgedui.editor.figures.SmartRectangleFigure;
import com.forgedui.editor.figures.TableViewFigure;
import com.forgedui.editor.figures.TitaniumBackgroundBorder;
import com.forgedui.editor.figures.WindowFigure;

/**
 * A child resize edit policy for the components at the editor. This policy is
 * installed from the AbstractConstrainedLayoutEditPolicy on its children so
 * that it will give them feedback shapes.
 * 
 * @author Tareq Doufish
 * 
 */
public class ChildElementResizableEditPolicy extends ResizableEditPolicy {

	public final static Color ghostFillColor = new Color(null, 31, 31, 31);

	protected IFigure createDragSourceFeedbackFigure() {
		IFigure figure = createFigure((GraphicalEditPart) getHost(), null);

		figure.setBounds(getInitialFeedbackBounds());

		addFeedback(figure);
		return figure;
	}

	protected IFigure createFigure(GraphicalEditPart part, IFigure parent) {
		IFigure child = getCustomFeedbackFigure(part);

		if (parent != null)
			parent.add(child);

		Rectangle childBounds = null;

		if (part instanceof ScrollViewEditPart) {
			ScrollViewEditPart sEditPart = (ScrollViewEditPart) part;
			ScrollViewFigure sFigure = (ScrollViewFigure) sEditPart.getFigure();
			IFigure elementFigure = sFigure.getContainer();
			childBounds = elementFigure.getBounds().getCopy();
		} else {
			childBounds = part.getFigure().getBounds().getCopy();
		}

		if (childBounds != null)
			child.setBounds(childBounds);

		Iterator<?> i = part.getChildren().iterator();

		while (i.hasNext()) {
			createFigure((GraphicalEditPart) i.next(), child);
		}
		return child;
	}

	protected IFigure getCustomFeedbackFigure(Object part) {
		GraphicalEditPart elementPart = (GraphicalEditPart) part;
		Figure figure = null;
		
		if (elementPart.getFigure().getChildren().size() > 0){
			int borderWidth = 0;
			Color borderColor = null;
			if (elementPart.getFigure().getBorder() instanceof TitaniumBackgroundBorder){
				TitaniumBackgroundBorder tbb = (TitaniumBackgroundBorder)elementPart.getFigure().getBorder();
				borderWidth = (int) tbb.getBorderWidth();
				borderColor = tbb.getBorderColor();
			}
			int titleBarHeight = 0;
			//necessary for titlebar delta
			if (elementPart instanceof WindowEditPart){
				titleBarHeight = ((WindowFigure)elementPart.getFigure()).getTitleBarHeight();
			} else if (elementPart.getFigure() instanceof TableViewFigure){
				TableViewFigure f = (TableViewFigure)elementPart.getFigure();
				titleBarHeight = f.hasHeader() ? f.getTitleHeight() : 0;
			}
			
			if (titleBarHeight > 0){
				final int headerHeight = titleBarHeight;
				figure = new SmartRectangleFigure(true, borderColor, borderWidth){
					public Rectangle getClientArea(Rectangle rect) {
						rect.setBounds(getBounds());
						rect.crop(super.getInsets());
						rect.setLocation(0, -headerHeight);
						return rect;
					};
					@Override
					public Insets getInsets() {
						return super.getInsets().getAdded(new Insets(headerHeight, 0,0,0));
					}
				};
			} else {
				figure = new SmartRectangleFigure(true, borderColor, borderWidth);
			}
			
			((RectangleFigure) figure).setXOR(true);
			((RectangleFigure) figure).setFill(true);
			figure.setBackgroundColor(ghostFillColor);
			figure.setForegroundColor(ColorConstants.white);
		} else {
			figure = new GhostImageFigure(elementPart.getFigure(), 200);
		}
		
		//This approach don't work because feedback should not paing children!!!
		/*
		// We have a special case here.
		if (elementPart instanceof ScrollViewEditPart) {
			ScrollViewEditPart sEditPart = (ScrollViewEditPart) elementPart;
			ScrollViewFigure sFigure = (ScrollViewFigure) sEditPart.getFigure();
			IFigure elementFigure = sFigure.getContainer();
			elementFigure.setLocation(sFigure.getLocation());
			figure = new GhostImageFigure(sFigure, 200);
		} else {
			figure = new GhostImageFigure(elementPart.getFigure(), 200);
			/*} else {
				figure = new SmartRectangleFigure(true);
				((RectangleFigure) figure).setXOR(true);
				((RectangleFigure) figure).setFill(true);
				figure.setBackgroundColor(ghostFillColor);
				figure.setForegroundColor(ColorConstants.white);
			}*/
		//}
		return figure;
	}

	/**
	 * A fix to the scaling bug.
	 */
	protected IFigure getFeedbackLayer() {
		return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
	}

	/**
	 * Ghost image for the feedback item. magical solution got from this link
	 * 
	 * http://nyssen.blogspot.com/2010/02/ghost-image-feedback-enhancing-support
	 * .html
	 */
	public class GhostImageFigure extends ImageFigure {
		private int alpha = -1;
		private ImageData ghostImageData;
		private boolean useLocalCoordinates = true;

		public GhostImageFigure(IFigure source, int alpha) {
			this.alpha = alpha;

			RGB transparency = new RGB(1, 1, 1);
			Rectangle sourceFigureRelativePrecisionBounds = new PrecisionRectangle(
					source.getBounds().getCopy());

			Image offscreenImage = new Image(Display.getCurrent(),
					sourceFigureRelativePrecisionBounds.width,
					sourceFigureRelativePrecisionBounds.height);

			Color transparentColor = new Color(null, transparency);
			GC gc = new GC(offscreenImage);
			SWTGraphics swtGraphics = new SWTGraphics(gc);
			swtGraphics.setBackgroundColor(transparentColor);
			swtGraphics.translate(-sourceFigureRelativePrecisionBounds.x,
					-sourceFigureRelativePrecisionBounds.y);
			swtGraphics.fillRectangle(sourceFigureRelativePrecisionBounds);
			source.paint(swtGraphics);

			ghostImageData = offscreenImage.getImageData();
			ghostImageData.transparentPixel = ghostImageData.palette
					.getPixel(transparency);

			transparentColor.dispose();
			offscreenImage.dispose();
			swtGraphics.dispose();
			gc.dispose();
		}
		
		/*@Override
		protected boolean useLocalCoordinates() {
			return this.useLocalCoordinates;
		}*/

		@Override
		protected void paintFigure(Graphics graphics) {
			IFigure figure = new RectangleFigure();
			((RectangleFigure) figure).setXOR(true);
			((RectangleFigure) figure).setFill(true);
			figure.setBackgroundColor(ghostFillColor);
			figure.setForegroundColor(ColorConstants.white);
			figure.setBounds(getBounds());
			figure.paint(graphics);
			/*Image feedbackImage = new Image(Display.getCurrent(),
					ghostImageData);
			graphics.setAlpha(alpha);
			graphics.setClip(getBounds().getCopy());
			graphics.drawImage(feedbackImage, 0, 0, ghostImageData.width,
					ghostImageData.height, getBounds().x, getBounds().y,
					getBounds().width, getBounds().height);
			feedbackImage.dispose();*/
		}
	}
}
