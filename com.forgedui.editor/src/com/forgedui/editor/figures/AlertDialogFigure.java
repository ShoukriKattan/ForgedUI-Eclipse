// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import com.forgedui.util.Utils;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AlertDialogFigure extends TitaniumTextFigure {
	
	private String explanation;
	
	protected Font titleFont;
	
	private DialogOptionFigure okButton;

	public AlertDialogFigure(DialogOptionFigure okButton) {
		setTextHorisontalAlign(Alignments.center);
		setTextVerticalAlign(Alignments.top);
		setMargin(7);
		this.okButton = okButton;
	}
	
	public void setExplanation(String text) {
		if (Utils.safeNotEquals(this.explanation, text)){
			this.explanation = text;
			setDirty(true);
		}
	}

	public void setOkText(String okText){
		if (Utils.safeNotEquals(okButton.getText(), okText)){
			okButton.setText(okText);
			setDirty(true);
		}
	}
	
	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		titleFont = createTitleFont();
		graphics.pushState();
		graphics.setFont(titleFont);
		paintTitle(graphics);
		graphics.popState();
		paintLines(graphics, getLines(), getLinesHolderRectangle());
		Rectangle expRect = getTextHolderRectangle();
		if (explanation != null && explanation.length() > 0){
			int titleHeight = getMargin()*2;
			if (getText() != null && getText().length() > 0){
				titleHeight += FigureUtilities.getStringExtents(getText(), titleFont).height;
			}
			expRect.height -= titleHeight;
			expRect.y += titleHeight;
			paintString(graphics, explanation, expRect);
		}
		if (okButton.getText() != null){
			graphics.setFont(okButton.getFont_());
			Dimension p = FigureUtilities.getStringExtents(okButton.getText(), okButton.getFont_());
			p.expand(20, 4);
			p.width = Math.min(p.width, expRect.width);
			p.height = 40;
			okButton.setBounds(new Rectangle(expRect.getBottomLeft().getTranslated(
					(expRect.width - p.width) / 2, -2 - p.height), p));
			okButton.paint(graphics);
		}
		titleFont.dispose();
	}

	public void paintTitle(Graphics graphics) {
		paintString(graphics, getText(), getTextHolderRectangle());
	}
	
	protected Rectangle getLinesHolderRectangle(){
		Rectangle r = getClientArea();
		if (useLocalCoordinates()){
			r.setLocation(getBounds().getLocation());
		}
		if (getText() != null && getText().length() > 0){
			Dimension textSize = FigureUtilities.getStringExtents(getText(), titleFont);
			r.y += textSize.height + getMargin();
			r.height -= textSize.height + getMargin();
		}
		return r;
	}
	
	protected Font createTitleFont(){
		FontData fd = getFont_().getFontData()[0];
		fd.setHeight(fd.getHeight() + 3);
		return new Font(null, fd);
	}
	
}


