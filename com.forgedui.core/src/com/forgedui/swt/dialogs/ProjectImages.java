package com.forgedui.swt.dialogs;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Representing the project images
 * 
 * @author Tareq Doufish
 *
 */
public class ProjectImages extends FileSystemImages {

    public ProjectImages() {
    	super();
    	setTitle("Resources images");
    }

    public ISourceImage[] getResources() {
        File file = getRootResourcesFolder();
        if (file != null) {
        	return getFileImageSources( file );
        } else { 
        	return null;
        }
    }
    
	/**
	 * Sets the tree input here.
	 */
	public File getRootResourcesFolder() { 
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
						try {
							folder.create(false, true, null);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					if (folder.exists()) {
						String folderPath = folder.getLocationURI().getPath();
						return new File(folderPath);
					} 
				}
			}
		}
		
		return null;
	}
	
	private IEditorPart getActiveEditor() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench
				.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		return activePage.getActiveEditor();
	}
}