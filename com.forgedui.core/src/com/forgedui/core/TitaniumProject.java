// LICENSE
package com.forgedui.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;

import com.forgedui.model.Diagram;
import com.forgedui.util.DiagramFileUtil;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitaniumProject {
	
	public static final String DIAGRAMS = "Diagrams";
	
	public static final String Tab_GROUP_DIAGRAMS = "TabGroupDiagrams";
	
	public static final String RESOURCES_FOLDER = "Resources";
	
	private IProject project;
	
	private Map<IFile, Diagram> diagrams = new HashMap<IFile, Diagram>();
	
	private Map<IFile, Diagram> tabDiagrams = new HashMap<IFile, Diagram>();
	
	private ProjectResourceDeltaVisitor resourceDeltaVisitor = new ProjectResourceDeltaVisitor();
	
	protected PropertyChangeSupport listeners;

	/**
	 * @param project
	 */
	public TitaniumProject(IProject project) {
		this.project = project;
		listeners = new PropertyChangeSupport(this);
		resourceDeltaVisitor.visitResource(project);
	}
	
	protected void initContainer(IContainer container){
		try {
			IResource[] res = container.members();
			for (IResource iResource : res) {
				if (iResource instanceof IContainer){
					initContainer((IContainer) iResource);
				} else if (iResource instanceof IFile){
					TitaniumProject.this.synchronizeTitaniumFiles(
							(IFile) iResource, IResourceDelta.ADDED);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void projectChanged(IResourceDelta delta) {
		resourceDeltaVisitor.visitDelta(delta);
	}
	
	protected boolean synchronizeTitaniumFiles(IFile file, int deltaKind) {
		if (file.getProject().equals(project) && file.getName().endsWith(".fui")){
			switch (deltaKind) {
				case IResourceDelta.ADDED :
					return this.addTitaniumFile(file);
				case IResourceDelta.REMOVED :
					return this.removeTitaniumFile(file);
				case IResourceDelta.CHANGED :
					return this.updateTitanium(file);
				default :
					break;  // only worried about added/removed/changed files
			}
		}

		return false;
	}
	
	/**
	 * @param file
	 * @return
	 */
	private boolean updateTitanium(IFile file) {
		Diagram oldDiagram = diagrams.get(file);
		addTitaniumFile(file);
		listeners.firePropertyChange(DIAGRAMS, oldDiagram, diagrams.get(file));
		return false;
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean removeTitaniumFile(IFile file) {
		removeDiagram(file);
		return false;
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean addTitaniumFile(IFile file) {
		Diagram diagram = DiagramFileUtil.getDiagramOrNull(file);
		if (diagram != null){
			addDiagram(diagram);
		} else {
			removeDiagram(file);
		}
		return true;
	}
	
	private void firePropertyChange(final String id, final Object old, final Object newObj){
		Display.getDefault().syncExec(
		  new Runnable() {
			  public void run(){
				  listeners.firePropertyChange(id, old, newObj);
			  }
		  });

	}

	public Collection<Diagram> getDiagrams(){
		return diagrams.values();
	}
	
	private boolean isTabGroupDiagram(Diagram diagram){
		if (diagram.getScreen().getTabGroup() != null){
			return true;
		}
		return false;
	}
	
	void addDiagram(Diagram diagram){
		Diagram old = diagrams.put(diagram.getFile(), diagram);
		firePropertyChange(DIAGRAMS, old, diagram);
		if (isTabGroupDiagram(diagram)){
			tabDiagrams.put(diagram.getFile(), diagram);
			firePropertyChange(Tab_GROUP_DIAGRAMS, old, diagram);
		}
	}
	
	void removeDiagram(IFile file){
		Diagram old = diagrams.remove(file);
		listeners.firePropertyChange(DIAGRAMS, old, null);
		if (old != null && isTabGroupDiagram(old)){
			tabDiagrams.remove(file);
			firePropertyChange(Tab_GROUP_DIAGRAMS, old, null);
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}
	
	public IFolder getReourcesFolder(){
		IFolder resourcesFolder = project.getFolder(RESOURCES_FOLDER);
		if (!resourcesFolder.exists()){
			try {
				resourcesFolder.create(true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return resourcesFolder;
	}
	
	class ProjectResourceDeltaVisitor implements IResourceDeltaVisitor, IResourceVisitor {
		
		protected void visitDelta(IResourceDelta delta) {
			try {
				delta.accept(this);
			} catch (CoreException ex) {
				// shouldn't happen - we don't throw any CoreExceptions
				throw new RuntimeException(ex);
			}
		}
		
		protected void visitResource(IResource res) {
			try {
				res.accept(this);
			} catch (CoreException ex) {
				// shouldn't happen - we don't throw any CoreExceptions
				throw new RuntimeException(ex);
			}
		}

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			return visit(delta.getResource(), delta.getKind());
		}

		@Override
		public boolean visit(IResource res) throws CoreException {
			return visit(res, IResourceDelta.ADDED);
		}
		
		private boolean visit(IResource res, int kind) throws CoreException {
			switch (res.getType()) {
				case IResource.ROOT :
				case IResource.PROJECT :
				case IResource.FOLDER :
					return true;  // visit children
				case IResource.FILE :
					TitaniumProject.this.synchronizeTitaniumFiles((IFile) res, kind);
					return false;  // no children
				default :
					return false;  // no children (probably shouldn't get here...)
			}
		}
		
	}

	/**
	 * @return
	 */
	public IProject getProject() {
		return project;
	}

	/**
	 * @return
	 */
	public Collection<Diagram> getTabGroupDiagrams() {
		return tabDiagrams.values();
	}

}
