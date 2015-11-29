//LICANSE
package com.forgedui.model.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.forgedui.util.Converter;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ColorPropertyDescriptor2 extends PropertyDescriptor {
	
	private ILabelProvider colorLabelProvider = new LabelProvider(){
		
		private Image colorImage;//one pixel image
		private RGB colorRGB;

		@Override
		public Image getImage(Object element) {
			if (element instanceof RGB) {
				RGB rgb = (RGB) element;
				if (!rgb.equals(colorRGB)){
					if (colorImage != null){
						colorImage.dispose();
					}
					colorRGB = rgb;
					PaletteData palette = new PaletteData(new RGB[]{rgb, new RGB(0,0,0)});
					ImageData data = new ImageData(1, 1, 1, palette);
					colorImage = new Image(null, data);
				}
			} else {
				if (colorImage != null){
					colorImage.dispose();
					colorImage = null;
				}
				colorRGB = null;
			}
			return colorImage;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof RGB) {
				RGB rgb = (RGB) element;
				return Converter.getHexColorValue(rgb);
			}
			return null;
		}
		
		protected void finalize() throws Throwable {
			if (colorImage != null){
				colorImage.dispose();
			}
			super.finalize();
		};
		
	};

	/**
	 * @param id
	 * @param displayName
	 */
	public ColorPropertyDescriptor2(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return colorLabelProvider;
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new ColorCellEditor2(parent);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }

}
