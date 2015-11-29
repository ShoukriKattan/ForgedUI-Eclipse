package com.forgedui.swt.dialogs;

import org.eclipse.swt.graphics.Image;

/**
 * Simulating an image.
 * 
 * @author Tareq Doufish
 *
 */
public interface ISourceImage {
        String getName();
        Image getImage();
        byte[] getData();
        boolean isImportNeeded();
        ISourceImage[] getChildren();
        ISourceImage getParent();
        boolean hasChildren();
        boolean isFolder();
}