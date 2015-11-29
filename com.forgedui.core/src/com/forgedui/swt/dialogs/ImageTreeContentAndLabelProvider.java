package com.forgedui.swt.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Label and content providers for the images at the tree of the images.
 */
class ImageTreeContentAndLabelProvider extends LabelProvider implements
		ITreeContentProvider {

	private static Image iconFolder;
	private static Image imageFile;

	public ImageTreeContentAndLabelProvider() {
		if (iconFolder == null)
			iconFolder = createImage("folder.gif");
		if (imageFile == null)
			imageFile = createImage("file.gif");
	}

	private Image createImage(String imageName) {
		return new Image(Display.getCurrent(), getClass().getClassLoader()
				.getResourceAsStream(imageName));
	}

	public Object[] getChildren(Object node) {
		if (node instanceof ImagesResource)
			return ((ImagesResource) node).getResources();
		else if (node instanceof ISourceImage)
			return ((ISourceImage) node).getChildren();
		return null;
	}

	public Object getParent(Object node) {
		if (node instanceof ImagesResource)
			return ((ImagesResource) node).getParent();
		else if (node instanceof ISourceImage)
			return ((ISourceImage) node).getParent();
		return null;
	}

	public boolean hasChildren(Object node) {
		if (node instanceof ImagesResource)
			return ((ImagesResource) node).hasResources();
		else if (node instanceof ISourceImage)
			return ((ISourceImage) node).hasChildren();
		return false;
	}

	public Object[] getElements(Object node) {
		List<FileSystemImages> elements = new ArrayList<FileSystemImages>();
		ProjectImages resources = new ProjectImages();
		elements.add(resources);
		return elements.toArray();
	}

	@Override
	public void dispose() {
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	@Override
	public Image getImage(Object node) {
		Image rImage = iconFolder;
		if (node instanceof ISourceImage) {
			ISourceImage sourceImage = (ISourceImage) node;
			if (sourceImage.isFolder()) { 
				return iconFolder;
			}
			if (!sourceImage.hasChildren())
				rImage = imageFile;
		}
		return rImage;
	}

	@Override
	public String getText(Object node) {
		if (node instanceof ImagesResource)
			return ((ImagesResource) node).getTitle();
		else if (node instanceof ISourceImage)
			return ((ISourceImage) node).getName();
		return node.toString();
	}

}
