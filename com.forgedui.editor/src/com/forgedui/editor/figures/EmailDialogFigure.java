// LICENSE
package com.forgedui.editor.figures;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.forgedui.editor.figures.TitaniumTextFigure.Alignments;
import com.forgedui.editor.util.SWTWordWrap;
import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class EmailDialogFigure extends TitaniumFigure {
	
	private Dimension resolution;
	
	private int radius = 10;
	
	private static final Color defaultBarColor = new Color(null, 100, 100, 100);
	private static final Color background = new Color(null, 200, 200, 200);
	private static final Color background2 = new Color(null, 250, 250, 250);
	private static final Color border = new Color(null, 20, 20, 20);
	private static final Color text = new Color(null, 140, 140, 140);
	
	private String strBarColor;
	private Color userBarColor;
	private String to,cc,bcc,subject,message;

	public EmailDialogFigure(Dimension resolution) {
		this.resolution = resolution;
	}
	
	public void setBarColor(String color) {
		if (Utils.safeNotEquals(strBarColor, color)){
			if (strBarColor != null){
				userBarColor.dispose();
			}
			userBarColor = (color == null)  ? null
				: Converter.getColorFromHexa(color);
			strBarColor = color;
			setDirty(true);
		}
	}
	
	protected Color getBarColor(){
		return userBarColor != null ? userBarColor : defaultBarColor;
	}
	
	public void setTo(String to) {
		if (Utils.safeNotEquals(this.to, to)){
			setDirty(true);
			this.to = to;
		}
	}

	public void setCc(String cc) {
		if (Utils.safeNotEquals(this.cc, cc)){
			setDirty(true);
			this.cc = cc;
		}
	}

	public void setBcc(String bcc) {
		if (Utils.safeNotEquals(this.bcc, bcc)){
			setDirty(true);
			this.bcc = bcc;
		}
	}

	public void setSubject(String subject) {
		if (Utils.safeNotEquals(this.subject, subject)){
			setDirty(true);
			this.subject = subject;
		}
	}

	public void setMessage(String message) {
		if (Utils.safeNotEquals(this.message, message)){
			setDirty(true);
			this.message = message;
		}
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		graphics.setAntialias(SWT.ON);
		paintCompose(graphics);
		paintBody(graphics);
		paindButtonsBar(graphics);
	}

	protected void paintCompose(Graphics graphics) {
		graphics.setBackgroundColor(getBarColor());
		Rectangle rect = new Rectangle(2, 2, resolution.width - 4, 30);
		graphics.fillRectangle(rect);
		graphics.setForegroundColor(ColorConstants.white);
		graphics.drawText("Compose", new Point(6, 6));
	}
	
	protected void paintBody(Graphics graphics) {
		graphics.setBackgroundColor(background);
		boolean drawCC = true;//(cc != null || bcc != null);
		int fieldsCount = drawCC ? 4 : 2;
		int compositeHeight = 1 + (fieldsCount * 35);
		Rectangle rect = new Rectangle(2, 34, resolution.width - 4, compositeHeight);
		graphics.fillRectangle(rect);
		
		Dimension fieldSize = new Dimension(resolution.width - 16, 30);
		graphics.setBackgroundColor(background2);
		graphics.setForegroundColor(border);
		rect = new Rectangle(new Point(8, 36), fieldSize);

		//to, cc, bcc, subject
		for (int i = 0; i < fieldsCount; i++) {
			graphics.fillRoundRectangle(rect, radius, radius);
			graphics.drawRoundRectangle(rect, radius, radius);
			rect.performTranslate(0, 35);
		}
		
		//message
		rect = new Rectangle(2, rect.y  , resolution.width - 4, resolution.height - 160);
		graphics.fillRoundRectangle(rect, radius, radius);
		graphics.drawRoundRectangle(rect, radius, radius);
		
		graphics.setForegroundColor(text);
		
		String[] titles = getTitles(drawCC);
		fieldSize.width -= 8;
		for (int i = 0; i < fieldsCount; i++) {
			Rectangle textRect = new Rectangle(new Point(12, 5 + (i + 1)*35), fieldSize);
			Drawer.drawString(graphics, titles[i], textRect, Alignments.left, Alignments.center);
		}
		
		paintMessage(graphics, rect);
	}
	
	protected void paintMessage(Graphics graphics, Rectangle parentRect) {
		String message = Utils.getString(this.message, "");
		List<String> text = SWTWordWrap.wrap(message, resolution.width - 24, graphics.getFont());
		if (text != null && text.size() > 0){
			int oneLineHeight = getMargin()
				+ FigureUtilities.getStringExtents("X", graphics.getFont()).height;
			graphics.pushState();
			graphics.clipRect(parentRect);

			int y = parentRect.y;
			for (int i = 0; y < parentRect.bottom() && i < text.size(); i++) {
				Rectangle lineRect = new Rectangle(parentRect.x, y, parentRect.width,
						oneLineHeight + getMargin());
				Drawer.drawString(graphics, text.get(i), lineRect, Alignments.left, Alignments.top, getMargin());
				y += oneLineHeight;
			}
			graphics.popState();
		}
	}
	
	protected String[] getTitles(boolean drawCC){
		return (drawCC ? new String[] { Utils.getString(to, "To"),
				Utils.getString(cc, "Cc"), Utils.getString(bcc, "Bcc"),
				Utils.getString(subject, "Subject") }
				: new String[] { Utils.getString(to, "To"),
						Utils.getString(subject, "Subject") });
	}
	

	/**
	 * @param graphics
	 */
	private void paindButtonsBar(Graphics graphics) {
		int panelHeight = 42;
		graphics.setBackgroundColor(defaultBarColor);
		Rectangle rect = new Rectangle(2, resolution.height - 2 - panelHeight,
				resolution.width - 4, panelHeight);
		graphics.fillRectangle(rect);
				
		int buttonWidth = (resolution.width - 20) / 7 ;
		int space = (resolution.width - buttonWidth * 7) / 7;
			
		graphics.setBackgroundColor(background2);
		graphics.setForegroundColor(border);
		rect = new Rectangle(5 + space, resolution.height - panelHeight + 2, buttonWidth*2, panelHeight - 10);
		graphics.fillRoundRectangle(rect, radius, radius);
		graphics.drawRoundRectangle(rect, radius, radius);
		
		rect.translate(buttonWidth*5 + space*2, 0);
		graphics.fillRoundRectangle(rect, radius, radius);
		graphics.drawRoundRectangle(rect, radius, radius);
		
		rect.translate(-buttonWidth*3 - space, 0);
		rect.width = buttonWidth*3;
		graphics.fillRoundRectangle(rect, radius, radius);
		graphics.drawRoundRectangle(rect, radius, radius);
		
		graphics.setForegroundColor(ColorConstants.black);
		graphics.drawText("Send", new Point(22 + space, resolution.height - panelHeight + 7));
		graphics.drawText("Save as Draft", new Point(12 + space*2 + buttonWidth*2, resolution.height - panelHeight + 7));
		graphics.drawText("Discard", new Point(14 + space*3 + buttonWidth*5, resolution.height - panelHeight + 7));
	}
	
	@Override
	public void dispose() {
		if (userBarColor != null) userBarColor.dispose();
		super.dispose();
	}
}