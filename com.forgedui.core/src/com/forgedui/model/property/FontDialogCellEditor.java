package com.forgedui.model.property;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.forgedui.model.titanium.properties.Font;
import com.forgedui.util.Converter;

/**
 * A font cell editor.
 * 
 * @author Tareq Doufish
 *
 */
public class FontDialogCellEditor extends NullabelDialogCellEditor {

	protected FontDialogCellEditor(Composite parent) {
		super(parent);
	}
	
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		return showCustomDialog();
		/*MessageDialog dialog = new MessageDialog(
				new Shell(),
				"Set Font",
				null,
				"Please select dialog to edit font data",
				MessageDialog.QUESTION,
				new String[]{IDialogConstants.CANCEL_LABEL, "Enter Manually", "System dialog"},
				2
		);

		int answer = dialog.open();
		if (answer == 2){//system
			return showSystemDialog();
		} else if (answer == 1){
			return showCustomDialog();
		}
		return null;*/
	}

	/*protected Object showSystemDialog(){
		FontDialog dialog = new FontDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		
		// Converting between our model font and the swt font !
		if (getValue() != null && getValue() instanceof Font) { 
			Font font = (Font) getValue();
			dialog.setFontList(new FontData[] {Converter.getFontDataFromFont(font)});
		}
		
		FontData data = dialog.open();
		return data;
	}*/
	
	protected Object showCustomDialog(){
		FontData currentValue = null;
		if (getValue() != null && getValue() instanceof Font) { 
			Font font = (Font) getValue();
			currentValue = Converter.getFontDataFromFont(font);
		}
		FontCustomDialog dialog = new FontCustomDialog(getControl().getShell(), currentValue);
		if (dialog.open() == Window.OK){
			return dialog.getFontData();
		}
		return null;
	}

}
