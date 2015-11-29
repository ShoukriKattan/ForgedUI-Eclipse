// LICENSE
package com.forgedui.editor.figures;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.View;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ElementFeedbackFigure extends Figure {
	
	private static Map<Key, IFigure> feedbacks =
		new HashMap<Key, IFigure>();
	
	public static IFigure getElementFeedBack(Element element){
		Key key = new Key(element);
		//if (feedbacks.get(key) == null){
			TitaniumFigure titanuimFigure =	FiguresFactory.createTitaniumFigure(element);

			if (titanuimFigure != null){
				titanuimFigure.setOpacity(150f);
				feedbacks.put(key, titanuimFigure);
			} else if (element instanceof View){
				ViewFigure figure = (ViewFigure) FiguresFactory.createFigure(element);
				figure.setOpaque(true);
				feedbacks.put(key, figure);
			} else {
				throw new IllegalArgumentException("Can't create feedback for " + element);
			}
		//}
		return feedbacks.get(key);
	}
	
	@Override
	public void paint(Graphics graphics) {
		graphics.setAdvanced(false);
		graphics.setXORMode(true);
		super.paint(graphics);
	}
	
	private static class Key {
		private Platform p;
		private Dimension r;
		private Class<?> c;
		public Key(Element e){
			this.p = e.getPlatform();
			this.r = e.getResolution();
			this.c = e.getClass();
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((c == null) ? 0 : c.hashCode());
			result = prime * result + ((p == null) ? 0 : p.hashCode());
			result = prime * result + ((r == null) ? 0 : r.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			return c.equals(other.c)
				&& (p == other.p)
				&& r.equals(other.r);
		}
		
	}

}
