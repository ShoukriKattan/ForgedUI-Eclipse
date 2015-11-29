// LICENSE
package com.forgedui.model.property;

import java.awt.GraphicsEnvironment;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class FontCustomDialog extends Dialog {
	
	private static final String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getAvailableFontFamilyNames();
	
	private FontData fontData = new FontData("Helvetica", 13, 0);

	private Text fontSize, errorMessageText;
	
	private Combo fontFamily;
	
	private Button bold, italic;
	
	private static Pattern integerPattern = Pattern.compile("\\d+");
	
	private ModifyListener modifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            validateInput();
        }
    };
    
    private SelectionListener selectionListener = new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		validateInput();
    	}
    };
    
	/**
	 * @param parentShell
	 */
	protected FontCustomDialog(Shell parentShell, FontData fontData) {
		super(parentShell);
		if (fontData != null){
			this.fontData = fontData;
		}
		
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Edit Font");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		// create a composite with standard margins and spacing
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(composite, SWT.NONE);
        label.setText("Font Family");
        fontFamily = new Combo(composite, SWT.BORDER);
        fontFamily.setItems(fontFamilies);
        fontFamily.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        label = new Label(composite, SWT.NONE);
        label.setText("Font Size");
        fontSize = new Text(composite, SWT.BORDER);
        fontSize.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        bold = new Button(composite, SWT.CHECK);
        bold.setText("Bold");
        /*bold.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL, GridData.BEGINNING,
        		false, false, 2, 1));*/
        
        italic = new Button(composite, SWT.CHECK);
        italic.setText("Italic");
        /*italic.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL, GridData.BEGINNING,
        		false, false, 2, 1));*/
        
        errorMessageText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
        errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL));
        errorMessageText.setBackground(errorMessageText.getDisplay()
                .getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.minimumWidth = 400;
        errorMessageText.setLayoutData(gd);
  
        initFont();
        fontFamily.addModifyListener(modifyListener);
        fontSize.addModifyListener(modifyListener);
        bold.addSelectionListener(selectionListener);
        italic.addSelectionListener(selectionListener);
        
        applyDialogFont(composite);
        return composite;
	}

	/**
	 * 
	 */
	protected void initFont() {
		if (fontData != null){
			fontFamily.setText(fontData.getName());
			if ((fontData.getStyle() & SWT.BOLD) != 0){
				bold.setSelection(true);
			}
			if ((fontData.getStyle() & SWT.ITALIC) != 0){
				italic.setSelection(true);
			}
			fontSize.setText("" + fontData.getHeight());
		}
	}

	protected void validateInput() {
        if (Utils.isEmpty(fontFamily.getText())){
        	setErrorMessage("Font family is required");
        } else if (Utils.isEmpty(fontSize.getText())){
        	setErrorMessage("Font size is required");
        } else if (!integerPattern.matcher(fontSize.getText()).matches()){
        	setErrorMessage("Font size should be an integer");
        } else {
        	setErrorMessage(null);
        	updateFontData();
        }
    }

    public void setErrorMessage(String errorMessage) {
    	if (errorMessageText != null && !errorMessageText.isDisposed()) {
    		errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$
    		// Disable the error message text control if there is no error, or
    		// no error text (empty or whitespace only).  Hide it also to avoid
    		// color change.
    		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=130281
    		boolean hasError = errorMessage != null && (StringConverter.removeWhiteSpaces(errorMessage)).length() > 0;
    		errorMessageText.setEnabled(hasError);
    		errorMessageText.setVisible(hasError);
    		errorMessageText.getParent().update();
    		// Access the ok button by id, in case clients have overridden button creation.
    		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=113643
    		Control button = getButton(IDialogConstants.OK_ID);
    		if (button != null) {
    			button.setEnabled(errorMessage == null);
    		}
    	}
    }
    
    protected void updateFontData(){
    	fontData.setName(fontFamily.getText());
    	int style = SWT.NORMAL;
    	if (bold.getSelection()){
    		style |= SWT.BOLD;
    	}
    	if (italic.getSelection()){
    		style |= SWT.ITALIC;
    	}
    	fontData.setStyle(style);
    	fontData.setHeight(Integer.parseInt(fontSize.getText()));
    }
    
    public FontData getFontData(){
    	return fontData;
    }
    
}
