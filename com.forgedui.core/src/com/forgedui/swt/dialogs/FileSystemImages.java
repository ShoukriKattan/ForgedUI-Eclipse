package com.forgedui.swt.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSystemImages extends ImagesResource {

	public FileSystemImages() {
		super();
	}

	static public ISourceImage[] getFileImageSources(File file) {
		if (file == null)
			return null;
		List<FileImageSource> fileList = new ArrayList<FileImageSource>();
		File files[] = file.listFiles();
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				if (!files[i].getName().startsWith(".")) {
					fileList.add(new FileImageSource(files[i]));
				}
			}
			return fileList.toArray(new ISourceImage[fileList.size()]);
		} else { 
			return null;
		}
	}
}