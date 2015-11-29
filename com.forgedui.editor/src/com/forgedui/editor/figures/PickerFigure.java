// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;

import com.forgedui.model.titanium.Picker.PickerType;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class PickerFigure extends TitaniumTextFigure {
	
	private static final Dimension BUTTON_SIZE = new Dimension(20, 10);
	
	private Insets insets = new Insets(10, 10, 10, 10);
	
	private boolean expanded = true;
	
	private RectangleFigure selection;
	
	private String value = "<Empty>";
	
	private PickerType type = PickerType.PICKER_TYPE_PLAIN;

	public PickerFigure() {
		super.setMargin(10);
		selection = new RectangleFigure();
		selection.setAlpha(50);
		selection.setBackgroundColor(ColorConstants.blue);
		//setLayoutManager(new PickerFigureLayout());
	}

	@Override
	public Insets getInsets() {
		return insets.getAdded(getBorder().getInsets(this));
	}
	
	/**
	 * @param expanded the expanded to set
	 */
	public void setExpanded(boolean expanded) {
		if (this.expanded != expanded){
			this.expanded = expanded;
			setDirty(true);
		}
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(PickerType type) {
		if (Utils.safeNotEquals(this.type, type)){
			this.type = type;
			setDirty(true);
		}
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		if (Utils.safeNotEquals(this.value, value)){
			this.value = value;
			setDirty(true);
		}
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		getBorder().setShowGradient(!expanded);
		if (expanded){
			super.paintFigure(graphics);
		} else {
			super.paintFigure(graphics);
			Rectangle bounds = getBounds().getCopy();
			bounds.width -= BUTTON_SIZE.width * 2;
			setTextHorisontalAlign(Alignments.left);
			paintString(graphics, value, bounds);
			paintButton(graphics);
		}
		setDirty(false);
	}
	
	@Override
	protected void paintChildren(Graphics graphics) {
		super.paintChildren(graphics);
		if (expanded && !selection.getBounds().equals(new Rectangle())){
			selection.paint(graphics);
		}
	}

	protected void paintButton(Graphics graphics) {
		graphics.setBackgroundColor(ColorConstants.black);
		Rectangle bounds = getBounds().getCopy();
		Path path = new Path(null);
		path.moveTo(bounds.right() - BUTTON_SIZE.width / 2,
				bounds.getCenter().y - BUTTON_SIZE.height / 2);
		path.lineTo(bounds.right()- 3*BUTTON_SIZE.width / 2,
				bounds.getCenter().y - BUTTON_SIZE.height / 2);
		path.lineTo(bounds.right() - BUTTON_SIZE.width,
				bounds.getCenter().y + BUTTON_SIZE.height / 2);
		graphics.fillPath(path);
	}

	public RectangleFigure getSelection() {
		return selection;
	}
	
	@Override
	public void setBounds(Rectangle rect) {
		// TODO Auto-generated method stub
		super.setBounds(rect);
	}

	class PickerFigureLayout extends AbstractLayout{

		@Override
		public void layout(IFigure parent) {
			Rectangle r = parent.getClientArea();
			
			int left = 0;
			IFigure f = null;
			for (int i = 0; i < parent.getChildren().size(); i++) {
				f = (IFigure) parent.getChildren().get(i);
				f.setLocation(r.getLocation().translate(left, 0));
				left += f.getSize().width;
				f.setSize(f.getSize().width, r.getSize().height);//set height
			}
			if (f != null && left < r.getSize().width){
				f.setSize(f.getSize().width + r.getSize().width - left, f.getSize().height);//set width to last column
			}
		}


		@Override
		protected Dimension calculatePreferredSize(IFigure container, int wHint,
				int hHint) {
			return getSize();
		}
		
		@Override
		public Object getConstraint(IFigure child) {
			return super.getConstraint(child);
		}
		
	}
	
}