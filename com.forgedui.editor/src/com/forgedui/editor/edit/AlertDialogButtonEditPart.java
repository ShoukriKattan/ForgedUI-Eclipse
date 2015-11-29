// LICENSE
package com.forgedui.editor.edit;

import org.eclipse.draw2d.geometry.Dimension;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.AlertDialog;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AlertDialogButtonEditPart extends OptionDialogButtonEditPart {
	
	protected String getBackgroundColor() {
		Element parent = getModel().getParent();
		AlertDialog dialog = (AlertDialog)parent;
		if (dialog.getOptionDialogButton(
				dialog.getCancel()) == getModel() && !getModel().getPlatform().isAndroid()){
			return "#111111";
		}
		return null;//standard uses default
	}
	
	protected Dimension getDialogButtonSize(Element e){
		return new Dimension((int)(e.getResolution().width*0.85), 48);
	}

}
