// LICENSE
package com.forgedui.editor.wizard;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import com.forgedui.core.ForgedUICorePlugin;
import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.model.Diagram;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.PlatformSupport;
import com.forgedui.util.DiagramFileUtil;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DiagramCreationPage extends WizardNewFileCreationPage {
	
	private static final String DEFAULT_EXTENSION = '.' + GUIEditorPlugin.FORGED_UI_EXTENSION;
	private final IWorkbench workbench;
	private Diagram diagram;
	private String baseFileName = "window";
	private boolean controlsCreated = false;
	
	private Combo platformField;
	
	private Combo resolutionField;
	private Button isLandscape;
	private Spinner customWidth, customHeight;
	private Button customResolution;
	private Dimension resolution;
	private IStructuredSelection selection;
	private static IPath activePath;
	/**
	 * Create a new wizard page instance.
	 * @param workbench the current workbench
	 * @param selection the current object selection
	 * @see NewWindowCreationWizard#init(IWorkbench, IStructuredSelection)
	 */
	DiagramCreationPage(IWorkbench workbench, IStructuredSelection selection) {
		super("GUI Window", selection);
		this.selection = selection;
		this.workbench = workbench;
		setTitle("Create a new " + DEFAULT_EXTENSION + " file");
		setDescription("Create a new " + DEFAULT_EXTENSION + " file");
		setFileExtension(GUIEditorPlugin.FORGED_UI_EXTENSION);
	}
	
	public void setBaseFileName(String baseFileName){
		this.baseFileName = baseFileName;
		setFileName(generateUniqueName(baseFileName));
	}
	
	public void createControl(Composite parent) {
		super.createControl(parent);
		controlsCreated = true;
		setFileName(generateUniqueName(baseFileName));
		setPageComplete(validatePage());
	}
	
	@Override
	protected void initialPopulateContainerNameField() {
		if(selection.iterator().hasNext()){
			super.initialPopulateContainerNameField();
			activePath = getContainerFullPath();
		}else{
			setContainerFullPath(activePath);
			super.initialPopulateContainerNameField();
		}
		
	}
	@Override
	protected void createAdvancedControls(Composite parent) {
		super.createAdvancedControls(parent);
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		c.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL));
		Label label = new Label(c, SWT.NONE);
		label.setText("Platform");
		// platform entry field
		platformField = new Combo(c, SWT.BORDER | SWT.READ_ONLY);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		platformField.setLayoutData(data);
		platformField
				.setItems(new String[] { Platform.iPhone.toString(),
						Platform.iPad.toString(),Platform.Android.toString() });
		platformField.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updatePlatform();
			}
		});
		
		SelectionListener resolutionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateResolution();
			}
		};
		
		Group group = new Group(c, SWT.None);
		group.setText("Resolution");
		group.setLayout(new GridLayout(3, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false, 2, 1));
		label = new Label(group, SWT.NONE);
		label.setText("Default");
		resolutionField = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		resolutionField.addSelectionListener(resolutionListener);
		isLandscape = new Button(group, SWT.CHECK);
		isLandscape.setText("Landscape");
		isLandscape.addSelectionListener(resolutionListener);
		
		customResolution = new Button(group, SWT.CHECK);
		customResolution.setText("Custom");
		customResolution.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resolutionField.setEnabled(!customResolution.getSelection());
				isLandscape.setEnabled(!customResolution.getSelection());
				customWidth.setEnabled(customResolution.getSelection());
				customHeight.setEnabled(customResolution.getSelection());
				if (customResolution.getSelection()){
					customWidth.setSelection(resolution.width);
					customHeight.setSelection(resolution.height);
				} else {
					updateResolution();
				}
			}
		});
		
		GridData gd2 = new GridData(75, -1);
		customWidth = new Spinner(group, SWT.NONE);
		customWidth.setValues(320, 100, 2000, 0, 10, 100);
		customWidth.setEnabled(false);
		customWidth.setLayoutData(gd2);
		customWidth.addSelectionListener(resolutionListener);
		
		customHeight = new Spinner(group, SWT.NONE);
		customHeight.setValues(480, 100, 2000, 0, 10, 100);
		customHeight.setEnabled(false);
		customHeight.setLayoutData(gd2);
		customHeight.addSelectionListener(resolutionListener);
		
//		if (platformField.getItemCount() > 1) {
//			platformField.select(1);// we have icons for iPhone, so
//									// select it
//		} else {
//			platformField.select(0);
//		}
		postAdvContCreation();
	}

	protected void postAdvContCreation() {
		platformField.select(0);
		updatePlatform();
	}
	
	public Combo getPlatformField() {
		return platformField;
	}
	
	protected void updatePlatform() {
		List<Dimension> dimensions = PlatformSupport
				.getResolutions(getPlatform());
		List<String> res = new LinkedList<String>();
		List<Dimension> portraitDimensions = new LinkedList<Dimension>();
		for (int i = 0; i < dimensions.size(); i++) {
			if (dimensions.get(i).width <= dimensions.get(i).height){
				res.add(dimensions.get(i).width + "x"
						+ dimensions.get(i).height);
				portraitDimensions.add(dimensions.get(i));
			}
		}
		resolutionField.setItems(res.toArray(new String[0]));
		resolutionField.setData(portraitDimensions);
		resolutionField.select(0);
		
		updateResolution();
		customWidth.setSelection(resolution.width);
		customHeight.setSelection(resolution.height);
		boolean enabled = false;
		if(getPlatform() == Platform.Android)
			enabled = true;
		customResolution.setEnabled(enabled);
	}
	
	@SuppressWarnings("unchecked")
	private void updateResolution() {
		if (resolutionField.isEnabled()){
			List<Dimension> dimensions = (List<Dimension>) resolutionField
					.getData();
			Dimension dimension = dimensions.get(resolutionField.getSelectionIndex());
			resolution = isLandscape.getSelection() ? dimension.getTransposed() : dimension;
		} else {
			resolution = new Dimension(customWidth.getSelection(),
					customHeight.getSelection());
		}
	}

	private String generateUniqueName(String base){
		if (controlsCreated && getContainerFullPath() != null){
			for (int i = 1;; i++) {
				final IPath containerPath = getContainerFullPath();
				IPath newFilePath = containerPath.append(base + i + DEFAULT_EXTENSION);
				if (!createFileHandle(newFilePath).exists()){
					return base + i + DEFAULT_EXTENSION;
				}
			}
		}
		return base + DEFAULT_EXTENSION;
	}
	
	
	public void setDefaultContent(Diagram diagram){
		this.diagram = diagram;
	}
	
	public Platform getPlatform(){
		return Platform.valueOf(platformField.getText());
	}
	
	public Dimension getResolution(){
		return resolution;
	}
	
	public Dimension getWindowResolution(){
		Platform platform = getPlatform();
		if(Platform.Android == platform)
			return resolution;
		else{
			List<Dimension> resolutions = PlatformSupport.getResolutions(platform);
			List<Dimension> window_resolutions = PlatformSupport.getWindowResolutions(platform);
			for (int i = 0; i < resolutions.size() ; i++) {
				if(resolution.equals(resolutions.get(i)))
					return window_resolutions.get(i);
			}
			return null;
		}
	}
	
	/**
	 * This method will be invoked, when the "Finish" button is pressed.
	 * @see NewWindowCreationWizard#performFinish()
	 */
	boolean finish() {
		// create a new file, result != null if successful
		IFile newFile = createNewFile();
		
		// open newly created file in the editor
		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		if (newFile != null && page != null) {
			try {
				IDE.openEditor(page, newFile, true);
			} catch (PartInitException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public IFile getFileHandle(){
		final IPath containerPath = getContainerFullPath();
		IPath newFilePath = containerPath.append(getFileName());
		return createFileHandle(newFilePath);
	}

	protected InputStream getInitialContents() {
		return DiagramFileUtil.getDiagramContents(diagram);
	}
		
	/**
	 * Return true, if the file name entered in this page is valid.
	 */
	private boolean validateFilename() {
		String name = getFileName().substring(0, getFileName().length() - 4);
		if (!ForgedUICorePlugin.JSS_NAME_PATTERN.matcher(name).matches()){
			setErrorMessage("The file name must be a valid JavaScript identifier");
			return false;
		}
		setErrorMessage(null);
		return true;
	}

	protected boolean validatePage() {
		return super.validatePage() && validateFilename();
	}
}