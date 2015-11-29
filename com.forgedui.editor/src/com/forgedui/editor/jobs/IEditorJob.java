package com.forgedui.editor.jobs;

import org.eclipse.jface.operation.IRunnableWithProgress;

public interface IEditorJob extends IRunnableWithProgress {

	public String getJobName();
	
	public String getJobDescription();
	
	public int getTotalTime();
	
}
