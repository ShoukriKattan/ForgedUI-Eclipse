package com.forgedui.util;

import org.eclipse.swt.graphics.ImageData;

/**
 * Interface that is used with the loading of the images.
 * 
 * @author Tareq Doufish
 *
 */
public interface IURLImageListener {

	public void imageLoaded(ImageData image);
	
	public void imageFailedToLoad();
	
}
