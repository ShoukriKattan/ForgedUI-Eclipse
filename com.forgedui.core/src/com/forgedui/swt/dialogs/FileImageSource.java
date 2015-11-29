package com.forgedui.swt.dialogs;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class FileImageSource implements ISourceImage {

	private static Map<File, Image> memoryImages = new HashMap<File, Image>();
	
	private final File file;

	public FileImageSource(File file) {
		this.file = file;
	}

	public byte[] getData() {
		return null;
	}

	/**
	 * This will create the image related to 
	 * the file selected.
	 */
	public Image getImage() {
		Image image = null;
		if (!file.isDirectory()) {
			image = memoryImages.get(file);
			if (image == null) {
				try {
					if (isSupported(file)) {
						image = createImage(file);
						memoryImages.put(file, image);
					}

				} catch (Exception e) {
				}
			}
		} else {
			image = null;
		}
		return image;
	}
	
	public boolean isFolder() { 
		return file.isDirectory();
	}

	public String getName() {
		return file.getName();
	}

	public boolean isImportNeeded() {
		return false;
	}

	public File getFile() {
		return file;
	}

	private String getFileExtension(File selected) {
		return selected.getAbsolutePath().substring(
				selected.getAbsolutePath().lastIndexOf('.'));
	}

	private boolean isSupported(File selected) {
		return selected.exists();
	}

	private Image createImage(File selected) throws MalformedURLException {
		Image image;
		image = new Image(Display.getCurrent(), ImageDescriptor.createFromURL(
				selected.toURI().toURL()).createImage().getImageData()
				.scaledTo(200, 200));
		return image;
	}

	public ISourceImage[] getChildren() {
		if( file.isDirectory())
			return FileSystemImages.getFileImageSources(file);
		return null;
	}

	public ISourceImage getParent() {
		return null;
	}

	public boolean hasChildren() {
		return file.isDirectory() ? file.listFiles().length > 0 : false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileImageSource) { 
			FileImageSource otherObject = (FileImageSource)obj;
			File otherFile = otherObject.getFile();
			if(otherFile != null) { 
				return this.file.getPath().equals(otherFile.getPath());
			}
		}
		return super.equals(obj);
	}
}