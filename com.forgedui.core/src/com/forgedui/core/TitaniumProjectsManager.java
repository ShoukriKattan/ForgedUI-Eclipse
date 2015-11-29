// LICENSE
package com.forgedui.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.ILock;
import org.eclipse.core.runtime.jobs.Job;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitaniumProjectsManager {
	
	private static final boolean DEBUG = false;
	
	private static final int RESOURCE_CHANGE_EVENT_TYPES =
		IResourceChangeEvent.POST_CHANGE;
	
	final ILock lock = this.getJobManager().newLock();
	
	final IResourceChangeListener resourceChangeListener = new ResourceChangeListener();
	
	List<TitaniumProject> titaniumProjects = new ArrayList<TitaniumProject>();
	
	public IJobManager getJobManager() {
		return Job.getJobManager();
	}
	
	void start() {
		try {
			this.lock.acquire();
			this.start_();
		} finally {
			this.lock.release();
		}
	}
	
	private void start_() {
		debug("*** Titanium project manager START ***"); //$NON-NLS-1$
		try {
			this.buildTitaniumProjects();
			this.getWorkspace().addResourceChangeListener(this.resourceChangeListener, RESOURCE_CHANGE_EVENT_TYPES);
		} catch (RuntimeException ex) {
			ForgedUICorePlugin.logError(ex);
			this.stop_();
		}
	}
	
	void addTitaniumProject(IProject project) {
		 titaniumProjects.add(this.buildTitaniumProject(project));
	}
	
	public TitaniumProject getTitaniumProject(IProject project) {
		try {
			this.lock.acquire();
			return this.getTitaniumProject_(project);
		} finally {
			this.lock.release();
		}
	}
	
	private TitaniumProject getTitaniumProject_(IProject project) {
		for (TitaniumProject titaniumProject : this.titaniumProjects) {
			if (titaniumProject.getProject().equals(project)) {
				return titaniumProject;
			}
		}
		return null;
	}
	 
	 private TitaniumProject buildTitaniumProject(IProject project) {
		 return new TitaniumProject(project);
	 }
	
	 private void buildTitaniumProjects() {
		try {
			this.buildTitaniumProjects_();
		} catch (CoreException ex) {
			ForgedUICorePlugin.logError(ex);
		}
	}
	
	private void buildTitaniumProjects_() throws CoreException {
		this.getWorkspace().getRoot().accept(new ResourceProxyVisitor(), IResource.NONE);
	}
	
	void projectChanged_(IResourceDelta delta) {
		Iterator<TitaniumProject> titaniumProjects = this.titaniumProjects.iterator();
		while (titaniumProjects.hasNext()) {
			TitaniumProject titaniumProject = (TitaniumProject) titaniumProjects
					.next();
			if (titaniumProject.getProject().equals(delta.getResource())){
				if (delta.getKind() == IResourceDelta.REMOVED){
					titaniumProjects.remove();
				} else {
					titaniumProject.projectChanged(delta);
				}
			}
			
		}
	}
	
	void stop() throws Exception {
		try {
			this.lock.acquire();
			this.stop_();
		} finally {
			this.lock.release();
		}
	}

	private void stop_() {
		debug("*** Titanium project manager STOP ***"); //$NON-NLS-1$
		this.getWorkspace().removeResourceChangeListener(this.resourceChangeListener);
		this.clearTitaniumProjects();
	}
	
	private void clearTitaniumProjects() {
		titaniumProjects.clear();
	}

	public IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	static void debug(String message) {
		if (DEBUG) {
			System.out.println(message);
		}
	}
	
	/**
	 * Visit the workspace resource tree, adding a Titanium project to the
	 * Titanium project manager for each open Eclipse project that has a Titanium nature.
	 */
	private class ResourceProxyVisitor implements IResourceProxyVisitor {
		ResourceProxyVisitor() {
			super();
		}

		public boolean visit(IResourceProxy resourceProxy) {
			switch (resourceProxy.getType()) {
				case IResource.ROOT :
					return true;  // all projects are in the "root"
				case IResource.PROJECT :
					this.processProject(resourceProxy);
					return false;  // no nested projects
				case IResource.FOLDER :
					return false;  // ignore
				case IResource.FILE :
					return false;  // ignore
				default :
					return false;
			}
		}

		private void processProject(IResourceProxy resourceProxy) {
			if (resourceProxy.isAccessible()) {  // the project exists and is open
				IProject project = (IProject) resourceProxy.requestResource();
				TitaniumProjectsManager.this.addTitaniumProject(project);
			}
		}
	}
	
	private class ResourceChangeListener implements IResourceChangeListener {

		ResourceChangeListener() {
			super();
		}

		public void resourceChanged(IResourceChangeEvent event) {
			switch (event.getType()) {
				case IResourceChangeEvent.POST_CHANGE :
					this.processPostChangeEvent(event);
					break;

				default :
					break;
			}
		}

		private void processPostChangeEvent(IResourceChangeEvent event) {
			debug("Resource POST_CHANGE"); //$NON-NLS-1$
			this.processPostChangeDelta(event.getDelta());
		}

		private void processPostChangeDelta(IResourceDelta delta) {
			IResource resource = delta.getResource();
			switch (resource.getType()) {
				case IResource.ROOT :
					this.processPostChangeRootDelta(delta);
					break;
				case IResource.PROJECT :
					this.processPostChangeProjectDelta(delta);
					break;
				case IResource.FOLDER :
					this.processPostChangeFolderDelta((IFolder) resource, delta);
					break;
				case IResource.FILE :
					this.processPostChangeFileDelta((IFile) resource, delta);
					break;
				default :
					break;
			}
		}

		// ***** POST_CHANGE ROOT
		private void processPostChangeRootDelta(IResourceDelta delta) {
			this.processPostChangeDeltaChildren(delta);
		}

		// ***** POST_CHANGE PROJECT
		private void processPostChangeProjectDelta(IResourceDelta delta) {
			if (delta.getKind() == IResourceDelta.ADDED){
				TitaniumProjectsManager.this.addTitaniumProject((IProject) delta.getResource());
			} else {
				TitaniumProjectsManager.this.projectChanged_(delta);
			}
			this.processPostChangeDeltaChildren(delta);
		}

		// ***** POST_CHANGE FOLDER
		private void processPostChangeFolderDelta(IFolder folder, IResourceDelta delta) {
			if (folder.getName().equals(".settings")) { //$NON-NLS-1$
				this.processPostChangeDeltaChildren(delta);
			}
		}

		// ***** POST_CHANGE FILE
		private void processPostChangeFileDelta(IFile file, IResourceDelta delta) {
			//TODO check for platform change
		}
		
		private void processPostChangeDeltaChildren(IResourceDelta delta) {
			for (IResourceDelta child : delta.getAffectedChildren()) {
				this.processPostChangeDelta(child);  // recurse
			}
		}

	}

}
