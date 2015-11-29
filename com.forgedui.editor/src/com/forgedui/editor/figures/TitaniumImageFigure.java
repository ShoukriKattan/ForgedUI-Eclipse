// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitaniumImageFigure extends TitaniumFigure {
	
	private Image img;
	private Rectangle imageBounds = new Rectangle();
	
	public TitaniumImageFigure(){
	}
	
	public TitaniumImageFigure(Image img){
		setImage(img);
	}
	
	/**
	 * @return The Image that this Figure displays
	 */
	public Image getImage() {
		return img;
	}
	
	public void setImage(Image image) {
		if (img != null)img.dispose();
		img = image;
		imageBounds = new Rectangle(image.getBounds());
		revalidate();
		repaint();
	}

	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		graphics.drawImage(getImage(), imageBounds, getBounds());
	}
	
	public Dimension getImageDimension(){
		return imageBounds.getSize();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		img.dispose();
	}

}
