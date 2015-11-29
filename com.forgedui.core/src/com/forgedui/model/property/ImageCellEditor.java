package com.forgedui.model.property;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.forgedui.core.ForgedUICorePlugin;
import com.forgedui.core.TitaniumProject;
import com.forgedui.swt.dialogs.FileImageSource;
import com.forgedui.swt.dialogs.ISourceImage;
import com.forgedui.swt.dialogs.ImageBrowserDialog;
import com.forgedui.util.Converter;

/**
 * An image cell editor.
 * 
 * @author Tareq Doufish
 *
 */
public class ImageCellEditor extends NullabelDialogCellEditor {

    private static final int DEFAULT_EXTENT = 16;

    private static final int GAP = 6;

    private Composite composite;

    protected Label imageLabel;
    
    /** A simple label that contains the image for the delete of this image and set it to null */
   // protected Label deleteLabel; 

    private Image image;
    
    /** Weather or not to support getting images from url or not */
    private boolean supportUrl = false;
    
    public ImageCellEditor(Composite parent) {
        this(parent, SWT.NONE);
    }

    public ImageCellEditor(Composite parent, int style) {
        super(parent, style);
    }
    
    public ImageCellEditor(Composite parent, boolean supportUrl) {
        this(parent, SWT.NONE);
        this.supportUrl = supportUrl;
    }
    
    public void changeToNull() { 
    	super.doSetValue((String)null);
    	this.focusLost();
    }
    
    
    /**
     * Internal class for laying out this cell editor.
     */
    protected class ColorCellLayout extends Layout {
        @Override
        public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
            if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
                return new Point(wHint, hHint);
            }
            Point colorSize = ImageCellEditor.this.imageLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
            return new Point((colorSize.x * 2 )+ GAP , colorSize.y);
        }

        @Override
        public void layout(Composite editor, boolean force) {
            Rectangle bounds = editor.getClientArea();
            Point deleteSize = new Point(0, 0);//ImageCellEditor.this.deleteLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
            Point colorSize = ImageCellEditor.this.imageLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
            int ty = (bounds.height) / 2;
            if (ty < 0) {
                ty = 0;
            }
            
            // Setting the bounds of those two elements ...
           // ImageCellEditor.this.deleteLabel.setBounds(-1, 0, deleteSize.x, deleteSize.y);
            ImageCellEditor.this.imageLabel.setBounds(deleteSize.x, 0, colorSize.x, colorSize.y);
        }
    }
    
    private ImageData createColorImage(Control w, String fileLocation) {

        GC gc = new GC(w);
        FontMetrics fm = gc.getFontMetrics();
        int size = fm.getAscent();
        gc.dispose();

        int indent = 6;
        int extent = DEFAULT_EXTENT;
        if (w instanceof Table) {
            extent = ((Table) w).getItemHeight() - 1;
        } else if (w instanceof Tree) {
            extent = ((Tree) w).getItemHeight() - 1;
        }

        if (size > extent) {
            size = extent;
        }

        int width = indent + size;
        int height = extent;

        ImageData data = null;
        
        // If its a url then we will not show anything.
        boolean isUrl = Converter.isStringUrl(fileLocation);
        if (!isUrl) {
	        try {
	            Image rImage = new Image(Display.getCurrent(), fileLocation);
	            data = rImage.getImageData().scaledTo(width, height);
	        } catch (Exception e) {
	        	ForgedUICorePlugin.logError(e);
	        }
	        return data;
        } else { 
        	return null;
        }
    }
     
    /*
     * (non-Javadoc) Method declared on DialogCellEditor.
     */
    @Override
    protected Control createContents(Composite cell) {
        Color bg = cell.getBackground();
        this.composite = new Composite(cell, getStyle());
        this.composite.setBackground(bg);
        this.composite.setLayout(new ColorCellLayout());
        
        composite.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.keyCode);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println(e.keyCode);
			}
		});
        
        cell.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("cell" + e.keyCode);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("cell" + e.keyCode);
			}
		});
        
        /*this.deleteLabel = new Label(this.composite, SWT.LEFT);
        this.deleteLabel.setBackground(bg);
        // Adding a listener for making it feel.
        this.deleteLabel.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				changeToNull();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});*/
        this.imageLabel = new Label(this.composite, SWT.LEFT);
        this.imageLabel.setBackground(bg);
        return this.composite;
    }

    @Override
    public void dispose() {
        if (this.image != null) {
            this.image.dispose();
            this.image = null;
        }
        super.dispose();
    }
    
    private ImageBrowserDialog imageBrowseDialog ;
    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
    	ISourceImage image = null;
    	String currentUrl = null;
    	Object val = getValue();
        // If we already have an image here.
        if (val != null && val instanceof String) { 
        	boolean isUrl = Converter.isStringUrl((String)val);
        	if (!isUrl)
        		image = new FileImageSource(new File((String)val));
        	else {
        		currentUrl = (String)val;
        	}
        }
    	if (imageBrowseDialog == null) {
    		imageBrowseDialog = new ImageBrowserDialog(image, this.supportUrl,currentUrl);
    	} else if (image != null) {
    		imageBrowseDialog.setSelection(image);
    	}
        int res = imageBrowseDialog.open();
    	if (res != Window.OK) {
    		return val;
    	}
    	
    	// Here is the case if we supporting url then we will have to deal with 
    	// it in a different way.
    	/*if (supportUrl ) {
    		if (imageBrowseDialog.getUrl() != null)
    			return imageBrowseDialog.getUrl();
    		else { 
    	        File file = imageBrowseDialog.getSelectedFile();
    	        if (file != null && !file.isDirectory()) {
    	        	return file.getAbsolutePath();
    	        }
    		}
    	} else { 
	        File file = imageBrowseDialog.getSelectedFile();
	        if (file != null && !file.isDirectory()) {
	        	return file.getAbsolutePath();
	        }
    	}
    	return null;*/
    	return imageBrowseDialog.getSelectedImagePath();
    }

    @Override
    protected void updateContents(Object value) {
        String url = (String) value;
        if (url == null)
            return;
        if (this.image != null) {
            this.image.dispose();
        }

        ImageData id = createColorImage(this.imageLabel.getParent().getParent(),
        		Converter.getImageFullPath(getProject(), url));
       // ImageData deleteImageData = createDeleteImage(this.deleteLabel.getParent().getParent());
        // there is a chance that this image is just null.
        if (id != null) {
	        ImageData mask = id.getTransparencyMask();
	        ImageData deleteMask = id.getTransparencyMask();
	        this.image = new Image(this.imageLabel.getDisplay(), id, mask);
	        this.imageLabel.setImage(this.image);
	        
	        //this.deleteLabel.setImage(new Image(this.deleteLabel.getDisplay(), deleteImageData, deleteMask));
        }
    }
    
    private TitaniumProject getProject(){
    	IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench
				.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();
		if (activeEditor != null) {
			IEditorInput editorInput = activeEditor.getEditorInput();
			if ((editorInput instanceof IFileEditorInput)) {
				IFile editorFile = ((IFileEditorInput) editorInput)
						.getFile();
				return (TitaniumProject) editorFile
					.getProject().getAdapter(TitaniumProject.class);
			}
		}
		return null;//how???
    }
}
