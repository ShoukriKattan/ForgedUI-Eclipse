//LICENSE
package com.forgedui.model.property;

import java.text.MessageFormat;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

import com.forgedui.util.Converter;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ColorCellEditor2 extends TextCellEditor {

	private Text text;
	
    /**
     * The button.
     */
    private Button button;
	
	/**
     * The editor control.
     */
    private Composite editor;
    
    private FocusListener buttonFocusListener;

	public ColorCellEditor2(Composite parent) {
		super(parent);
	}

	@Override
	protected Control createControl(Composite parent) {
		Font font = parent.getFont();
        Color bg = parent.getBackground();

        editor = new Composite(parent, getStyle());
        editor.setFont(font);
        editor.setBackground(bg);
        editor.setLayout(new DialogCellLayout());

        text = (Text) super.createControl(editor);

        button = createButton(editor);
        button.setFont(font);

        button.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.character == '\u001b') { // Escape
                    fireCancelEditor();
                }
            }
        });
        
        button.addFocusListener(getButtonFocusListener());
        
        button.addSelectionListener(new SelectionAdapter() {
            /* (non-Javadoc)
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetSelected(SelectionEvent event) {
            	// Remove the button's focus listener since it's guaranteed
            	// to lose focus when the dialog opens
            	button.removeFocusListener(getButtonFocusListener());
                
            	Object newValue = openDialogBox(editor);
            	
            	// Re-add the listener once the dialog closes
            	button.addFocusListener(getButtonFocusListener());

            	if (newValue != null) {
                    boolean newValidState = isCorrect(newValue);
                    if (newValidState) {
                        markDirty();
                        doSetValue(newValue);
                    } else {
                        // try to insert the current value into the error message.
                        setErrorMessage(MessageFormat.format(getErrorMessage(),
                                new Object[] { newValue.toString() }));
                    }
                    fireApplyEditorValue();
                }
            }

			
        });

        setValueValid(true);

        return editor;
	}
	
	@Override
	protected void focusLost() {
		if (isActivated()) {
			if (button != null && !button.isDisposed() && !button.isFocusControl()) {
	    		if (text != null && !text.isDisposed() && !text.isFocusControl()){
	    			fireApplyEditorValue();
	    			deactivate();
	    		}
	    	}
		}
	}
	
	protected Object openDialogBox(Control cellEditorWindow) {
		ColorDialog dialog = new ColorDialog(cellEditorWindow.getShell());
        Object value = getValue();
        if (value != null) {
			dialog.setRGB((RGB) value);
		}
        value = dialog.open();
        RGB rgb = dialog.getRGB();//return the same if cancelled
        if (rgb != null){
        	return rgb;//rgbToString(rgb);
        }
        return null;
	}
	
	private String rgbToString(RGB rgb){
		/*int intRGB = ((rgb.red & 0xFF) << 16)
    					| ((rgb.green & 0xFF) << 8)
    					| ((rgb.blue & 0xFF) << 0);
		return "" + Integer.toHexString(intRGB);*/
		String str = Converter.getHexColorValue(rgb);
		if (str.startsWith("#")){
			return str.substring(1);
		}
		return str;
	}
	
	private RGB stringToRGB(String strRGB){
		if (strRGB != null){
			if (!strRGB.startsWith("#")){
				strRGB = "#" + strRGB;
			}
			try {
				int rgb = Integer.decode(strRGB);
				return new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF,
						(rgb >> 0) & 0xFF);
			} catch (NumberFormatException e) {

			}
		}
		return null;
		
	}
	
	@Override
	protected Object doGetValue() {
		String value = text.getText();
		if (value != null){
			return stringToRGB(value);
		}
		return super.doGetValue();
	}
	
	@Override
	protected void doSetValue(Object value) {
		if (value instanceof RGB) {
			super.doSetValue(rgbToString((RGB) value));
		} else {
			super.doSetValue("");
		}
	}
	
    private FocusListener getButtonFocusListener() {
    	if (buttonFocusListener == null) {
    		buttonFocusListener = new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					ColorCellEditor2.this.focusLost();
				}
    		};
    	}
    	
    	return buttonFocusListener;
	}

    protected Button createButton(Composite parent) {
        Button result = new Button(parent, SWT.DOWN);
        result.setText("..."); //$NON-NLS-1$
        return result;
    }
    
    public void deactivate() {
    	if (button != null && !button.isDisposed()) {
    		button.removeFocusListener(getButtonFocusListener());
    	}
    	
		super.deactivate();
	}

	private class DialogCellLayout extends Layout {
        public void layout(Composite editor, boolean force) {
            Rectangle bounds = editor.getClientArea();
            Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
            if (text != null) {
            	text.setBounds(0, 0, bounds.width - size.x, bounds.height);
			}
            button.setBounds(bounds.width - size.x, 0, size.x, bounds.height);
        }

        public Point computeSize(Composite editor, int wHint, int hHint,
                boolean force) {
            if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
            Point contentsSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT,
                    force);
            Point buttonSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT,
                    force);
            // Just return the button width to ensure the button is not clipped
            // if the label is long.
            // The label will just use whatever extra width there is
            Point result = new Point(buttonSize.x, Math.max(contentsSize.y,
                    buttonSize.y));
            return result;
        }
    }

	
}