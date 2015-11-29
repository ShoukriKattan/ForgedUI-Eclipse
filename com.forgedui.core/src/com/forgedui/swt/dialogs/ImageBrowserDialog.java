package com.forgedui.swt.dialogs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.forgedui.core.TitaniumProject;
import com.forgedui.util.Converter;

/**
 * A dialog for showing images at the work space of the editor.
 * 
 * @author Tareq Doufish
 * 
 */
public class ImageBrowserDialog extends TitleAreaDialog {

	private static final String ORIGINAL_SIZED_IMAGE = "originalSizedImage";
	private ImageTree imagesTree;
	private ISourceImage selectedResource;
	private Label canvas;
	
	private Label urlCanvas;

	/** The scaling buttons of the image viewer used for the canvas */
	private Button importToWorkSpaceButton;
	private Button fitButton;
	private Button originalButton;
	private Button scaleUp;
	private Button scaleDown;

	/** Can be configured */
	private float scaleFactor = 2.0f;

	private ISourceImage currentSelection;
	
	private boolean supportUrl = true;
	
	/** Weather we already had a url or not */
	private String currentUrl;
	
	private CTabFolder tabsFolder;
	
	private Composite imageCanvasComposite;
	private ScrolledComposite imageContainer;

	/**
	 * @param arg0
	 */
	public ImageBrowserDialog(ISourceImage selection) {
		super(Display.getCurrent().getActiveShell());
		currentSelection = selection;
	}

	public ImageBrowserDialog(ISourceImage selection, boolean supportUrl, String currentUrl) { 
		this(selection);
		this.supportUrl = supportUrl;
		this.currentUrl = currentUrl;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(640, 480);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Available images");
		setMessage("All the images available in the editor path and resources.");
		getShell().setText("Available images");

		Composite composite = null;
		
		Composite area = (Composite) super.createDialogArea(parent); 
		Composite container = new Composite(area, SWT.NONE); 
		container.setLayout(new GridLayout()); 
		container.setLayoutData(new GridData(GridData.FILL_BOTH)); 
		
		// If we are going to support the URL, then 
		// we will create the tab for showing the url bar.
		if (supportUrl) {
			tabsFolder = new CTabFolder(container, SWT.NONE);
			tabsFolder.setLayoutData(new GridData(GridData.FILL_BOTH)); 
			CTabItem tabOne = new CTabItem(tabsFolder, SWT.NONE);
			tabOne.setText("Workspace images");
			
			composite = new Composite(tabsFolder, SWT.NONE); 
			composite.setLayout(new GridLayout(2,  false)); 
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			tabOne.setControl(composite); 
			
			createUrlTab();
		} else {
			composite = new Composite(container, SWT.NONE); 
			composite.setLayout(new GridLayout(2,  false)); 
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		
		// The buttons composite.
		Composite toolBar = new Composite(composite, SWT.None);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		toolBar.setLayout(new GridLayout(5, false));
		toolBar.setLayoutData(data);

		GridData data2 = new GridData();//make all buttons equals
		importToWorkSpaceButton = new Button(toolBar, SWT.BORDER);
		importToWorkSpaceButton.setText("Import");
		importToWorkSpaceButton.setToolTipText("Import images as project resources");
		importToWorkSpaceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// We will show the file chooser to open 
				// the images to get imported to the work space of the editor.
				onFileOpen();
			}
		});
		importToWorkSpaceButton.setLayoutData(data2);
		
		scaleUp = new Button(toolBar, SWT.PUSH);
		scaleUp.setText("Zoom In");
		scaleUp.setToolTipText("Make image bigger");
		scaleUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				scaleUp();
			}
		});
		scaleUp.setLayoutData(data2);

		scaleDown = new Button(toolBar, SWT.PUSH);
		scaleDown.setText("Zoom Out");
		scaleDown.setToolTipText("Make image bigger");
		scaleDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				scaleDown();
			}
		});
		scaleDown.setLayoutData(data2);

		fitButton = new Button(toolBar, SWT.PUSH);
		fitButton.setText("Fit");
		fitButton.setToolTipText("Fit full container");
		fitButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fitScale();
			}
		});
		fitButton.setLayoutData(data2);

		originalButton = new Button(toolBar, SWT.PUSH);
		originalButton.setText("Original Size");
		originalButton.setToolTipText("Scale to Original Size");
		originalButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fitOriginal();
			}
		});
		originalButton.setLayoutData(data2);

		imagesTree = new ImageTree(composite);
		imagesTree.getTreeViewer().addSelectionChangedListener(
				new ImageTreeSelectionListener(this));
		imagesTree.getTreeViewer().addSelectionChangedListener(
				new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						Image originalSizedImage = getOriginalSizedImage();
						if (originalSizedImage != null) {
							originalSizedImage.dispose();
							canvas.setData(ORIGINAL_SIZED_IMAGE, null);
						}
					}
				});

		imagesTree.getTreeViewer().expandAll();

		ScrolledComposite imageContainer = new ScrolledComposite(composite,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		imageContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		imageContainer.setExpandHorizontal(true);
		imageContainer.setExpandVertical(true);
		canvas = new Label(imageContainer, SWT.NONE);
		imageContainer.setContent(canvas);
		canvas.setText("...");
		canvas.setAlignment(SWT.CENTER);
		canvas.setSize(canvas.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		if (currentSelection != null)
			setSelection(currentSelection);
		
		if (this.currentUrl != null) {
			// First select the tab then show the image.
			selectImageTab();
			fillImageUrlText();
			getWebImage(currentUrl);
		}
		
		return area;
	}

	private void scaleImage(float factor) {
		Image image = getOriginalSizedImage();
		if (image != null) {
			Image currentImage = canvas.getImage();
			int imageWidth = currentImage.getBounds().width;
			int imageHight = currentImage.getBounds().height;

			int width = (int) (imageWidth * factor);
			int height = (int) (imageHight * factor);
			ImageData newImage = image.getImageData().scaledTo(width, height);
			canvas.setImage(new Image(Display.getCurrent(), newImage));
			canvas.setSize(new Point(width, height));
		}
	}

	private Image getOriginalSizedImage() {
		Image image = (Image) canvas.getData(ORIGINAL_SIZED_IMAGE);
		if (image == null) {
			Image currentImage = canvas.getImage();
			if (currentImage != null)
				image = new Image(Display.getCurrent(), currentImage,
						SWT.IMAGE_COPY);
			canvas.setData(ORIGINAL_SIZED_IMAGE, image);
		}
		return image;
	}

	private Button loadImagesButton;
	private Text urlText;
	
	private CTabItem urlImageCanvasTabItem;
	
	/**
	 * This will create the tab that will contain the loading of the url item.
	 */
	private void createUrlTab() { 
		urlImageCanvasTabItem = new CTabItem(tabsFolder, SWT.NONE);
		urlImageCanvasTabItem.setText("Url image");
		Composite composite = new Composite(tabsFolder, SWT.NONE); 
		composite.setLayout(new GridLayout(2,  false)); 
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		urlImageCanvasTabItem.setControl(composite); 
		
		urlText = new Text(composite, SWT.BORDER);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		loadImagesButton = new Button(composite, SWT.NONE);
		loadImagesButton.setText("Preview");
		loadImagesButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				String url = urlText.getText();
				if (url != null && url.length() > 0) { 
					// Getting the content of the web page !>
					getWebImage(url);
				}
			}

		});

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.heightHint = 20;
		
		// Finally create the image previewer here.
		imageCanvasComposite = new Composite(composite, SWT.NONE); 
		imageCanvasComposite.setLayout(new GridLayout(1,  false)); 
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		imageCanvasComposite.setLayoutData(data);
		
		imageContainer = new ScrolledComposite(imageCanvasComposite,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		imageContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		imageContainer.setExpandHorizontal(true);
		imageContainer.setExpandVertical(true);
		urlCanvas = new Label(imageContainer, SWT.NONE);
		imageContainer.setContent(urlCanvas);
		urlCanvas.setText("");
		urlCanvas.setAlignment(SWT.CENTER);
		urlCanvas.setSize(urlCanvas.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	private void selectImageTab() { 
		if (tabsFolder != null && !tabsFolder.isDisposed() && urlImageCanvasTabItem != null && !urlImageCanvasTabItem.isDisposed())
			this.tabsFolder.setSelection(urlImageCanvasTabItem);
	}
	
	private void fillImageUrlText() { 
		urlText.setText(currentUrl);
	}
	
	private String imageUrl ;
	
	/**
	 * This will load the image from the web url
	 * @param url
	 */
	private void getWebImage(String url) {
		if (this.urlCanvas.getImage() != null){
			this.urlCanvas.getImage().dispose();
			this.urlCanvas.setImage(null);
			this.urlCanvas.redraw();
		}
		urlCanvas.setText("Loading image please wait...");
		enableDisableLoading(false);
		
		Image image = Converter.getImageFromUrl(url);
		if (image != null) {
			this.imageUrl = url;
			urlCanvas.setText("");
			if (image.getBounds().width > imageCanvasComposite.getBounds().width
					|| image.getBounds().height > imageCanvasComposite.getBounds().height){
				double scaleFactor = Math.max(image.getBounds().width/imageContainer.getBounds().width,
						image.getBounds().height/imageContainer.getBounds().height);
				Image scaledImage = new Image(null, image.getImageData().scaledTo(
						(int)(image.getBounds().width/scaleFactor),
						(int)(image.getBounds().height/scaleFactor)));
				image.dispose();
				showUrlImage(scaledImage);
			} else {
				showUrlImage(image);
			}
		} else { 
			this.imageUrl = null;
			urlCanvas.setText("Can't load the image");
		}
		enableDisableLoading(true);
	}

	private void enableDisableLoading(boolean enable) { 
		this.urlText.setEnabled(enable);
		this.loadImagesButton.setEnabled(enable);
	}
	/**
	 * Scales up the image
	 */
	private void scaleUp() {
		scaleImage(scaleFactor);
	}

	/**
	 * Scales down the scale of the image.
	 */
	private void scaleDown() {
		scaleImage(1 / scaleFactor);
	}

	/**
	 * Setting the selection if we open a file with an already imaged at the
	 * figure.
	 * 
	 * @param image
	 */
	public void setSelection(ISourceImage image) {
		imagesTree.getTreeViewer().setSelection(new StructuredSelection(image));
	}

	/**
	 * Fits the scale of the image to fill in the while canvas.
	 */
	private void fitScale() {
		Image originalSizedImage = getOriginalSizedImage();
		if (originalSizedImage != null) {
			int canvasWidth = canvas.getBounds().width;
			int canvasHight = canvas.getBounds().height;
			ImageData newImage = originalSizedImage.getImageData().scaledTo(
					canvasWidth, canvasHight);
			canvas.setImage(new Image(Display.getCurrent(), newImage));
		}
	}

	private void fitOriginal() {
		Image originalSizedImage = getOriginalSizedImage();
		if (originalSizedImage != null) {
			canvas.setImage(new Image(Display.getCurrent(), originalSizedImage,
					SWT.IMAGE_COPY));
		}
	}

	public File getSelectedFile() {
		if (selectedResource != null) {
			if (selectedResource instanceof FileImageSource)
				return ((FileImageSource) selectedResource).getFile();
		}
		return null;
	}
	
	/**
	 * 
	 * @return url or file path relative to Resources folder
	 */
	public String getSelectedImagePath() {
		if (supportUrl && getUrl() != null){
			return getUrl();
		} else if (getSelectedFile() != null){
			IEditorPart activeEditor = getActiveEditor();
			if (activeEditor != null) {
				IEditorInput editorInput = activeEditor.getEditorInput();
				if ((editorInput instanceof IFileEditorInput)) {
					IFile editorFile = ((IFileEditorInput) editorInput)
							.getFile();
					TitaniumProject titProject = (TitaniumProject) editorFile
						.getProject().getAdapter(TitaniumProject.class);
					IPath path = new Path(getSelectedFile().getAbsolutePath());
	    	        path = path.makeRelativeTo(titProject.getReourcesFolder().getLocation());
	    	        return path.toPortableString();
				}
			}
		}
		return null;
	}

	@Override
	protected void okPressed() {
		if(getUrl() != null) { 
			super.okPressed();
		} else {
			if (selectedResource != null) {
				super.okPressed();
			}
			// TODO: We will have to show some messages... to tell the user to 
			// select a file please.
		}
	}

	public void setSelectedResource(ISourceImage selected) {
		selectedResource = selected;
	}

	public void showImage(Image image) {
		try {
			canvas.setImage(image);
		} catch (Exception e) {
		}
	}

	public void showUrlImage(Image image) {
		this.urlCanvas.setImage(image);
	}
	
	/**
	 * This method will try to import an image to the work space at the editor,
	 * and put it at the resources folder.
	 * 
	 * @param resourceImageData
	 */
	public void importImageToWorkspace(File newFile) {
		String fileName = newFile.getName();
		InputStream is = null;
		try {
			is = new FileInputStream(newFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		selectedResource = null;
		
		if (is == null)
			return;
		try {
			IEditorPart activeEditor = getActiveEditor();
			if (activeEditor != null) {
				IEditorInput editorInput = activeEditor.getEditorInput();
				if ((editorInput instanceof IFileEditorInput)) {
					IFile editorFile = ((IFileEditorInput) editorInput)
							.getFile();
					IProject project = editorFile.getProject();
					if (project != null) {
						IFolder folder = project.getFolder("Resources");
						if (!folder.exists())
							folder.create(false, true, null);
						IFile file = folder.getFile(fileName);
						if (!file.exists()) {
							file.create(is, false,
									null);
							ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(
									null);
							progressMonitorDialog.open();
							project.refreshLocal(IResource.DEPTH_INFINITE,
									progressMonitorDialog.getProgressMonitor());
							progressMonitorDialog.close();
							
							// Refreshing the tree to show the new inputs.
							imagesTree.refreshInput();
							imagesTree.getTreeViewer().expandAll();
						}
					}
				}

			}
		} catch (CoreException e) {
		}
	}

	/**
	 * This will return the added url
	 * @return
	 */
	public String getUrl() { 
		return imageUrl;
	}
	
	private IEditorPart getActiveEditor() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench
				.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		return activePage.getActiveEditor();
	}

	public class ImageTreeSelectionListener implements
			ISelectionChangedListener {

		private final ImageBrowserDialog imagesBrowseDialog;

		public ImageTreeSelectionListener(ImageBrowserDialog imagesBrowseDialog) {
			this.imagesBrowseDialog = imagesBrowseDialog;
		}

		public void selectionChanged(SelectionChangedEvent event) {
			Object firstElement = null;
			if (event.getSelection() instanceof TreeSelection) {
				TreeSelection treeSelection = (TreeSelection) event
						.getSelection();
				firstElement = treeSelection.getFirstElement();
			}

			if (firstElement instanceof ISourceImage) {
				Image image = ((ISourceImage) firstElement).getImage();
				imagesBrowseDialog
						.setSelectedResource((ISourceImage) firstElement);
				imagesBrowseDialog.showImage(image);
			} else {
				imagesBrowseDialog.setSelectedResource(null);
				imagesBrowseDialog.showImage(null);
			}
		}

	}
	
	/**
	 * Opens the file dialog to create the imports here
	 */
	public void onFileOpen() {
		FileDialog fileChooser = new FileDialog(Display.getCurrent()
				.getActiveShell(), SWT.MULTI);
		fileChooser.setText("Choose image");
		// fileChooser.setFilterPath("");
		fileChooser
				.setFilterExtensions(new String[] { "*.gif; *.jpg; *.png; *.ico; *.bmp" });
		fileChooser.setFilterNames(new String[] { "SWT image"
				+ " (gif, jpeg, png, ico, bmp)" });
		String filename = fileChooser.open();
		List<String> paths = new ArrayList<String>();
		if (filename != null) {
			String filePath = null;
			String[] files = fileChooser.getFileNames();
			for (int i = 0; i < files.length ; i++) {
				filePath = new String();
				filePath += fileChooser.getFilterPath();
				if (filePath.charAt(filePath.length() - 1) != File.separatorChar) {
					filePath += File.separatorChar;
				}
				filePath += files[i];
				paths.add(filePath);
			}
		}	
		for (String path : paths)	 { 
			File file = new File(path);
			if (file != null)
				importImageToWorkspace(new File(path));
		}
	}
}