package com.forgedui.swt.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ImageTree {

	private TreeViewer tv;
	@SuppressWarnings("unchecked")
	private List selectedFiles;
	private Listener doubleClickListener;

	public ImageTree(final Composite parent) {
		// Create the tree viewer to display the file tree
		tv = new TreeViewer(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.widthHint = parent.getShell().getSize().x / 4;
		tv.getTree().setLayoutData(data);

		setupContentAndLabelProvider();
		setupFilter();
		setupListeners();
		
		// Set up the input for this item.
		ProjectImages images = new ProjectImages();
		tv.setInput(images.getResources());
	}
	
	public void refreshInput() { 
		// Set up the input for this item.
		ProjectImages images = new ProjectImages();
		tv.setInput(images.getResources());
	}

	private void setupListeners() {
		tv.addDoubleClickListener(new IDoubleClickListener() {
			@SuppressWarnings("unchecked")
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event
							.getSelection();

					selectedFiles = new ArrayList();

					for (Iterator iterator = selection.iterator(); iterator
							.hasNext();) {
						Object domain = iterator.next();

						if (domain instanceof File) {

							File file = (File) domain;
							if (doubleClickListener != null) {
								Event evnt = new Event();
								evnt.data = file;
								doubleClickListener.handleEvent(evnt);
							}

						}
					}
				}
			}
		});

		tv.addSelectionChangedListener(new ISelectionChangedListener() {
			@SuppressWarnings("unchecked")
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event
							.getSelection();

					selectedFiles = new ArrayList();

					for (Iterator iterator = selection.iterator(); iterator
							.hasNext();) {
						Object domain = iterator.next();

						if (domain instanceof File) {
							selectedFiles.add(domain);
						}
					}
				}
			}
		});
	}

	private void setupFilter() {
	}

	private void setupContentAndLabelProvider() {
		ImageTreeContentAndLabelProvider provider = new ImageTreeContentAndLabelProvider();
		tv.setContentProvider(provider);
		tv.setLabelProvider(provider);
	}

	public TreeViewer getTreeViewer() {
		return tv;
	}
}