package com.forgedui.swt.dialogs;

/**
 * An image resource, might be from local file system
 * or from a jar resource, or from any place.
 * 
 * @author Tareq Doufish
 *
 */
public abstract class ImagesResource {

    private String title;

    public ImagesResource() {
            this.title = "";
    }

    public ISourceImage[] getResources() {
            return null;
    }

    public boolean hasResources() {
            Object[] resources = getResources();
            if (resources != null)
                    return resources.length > 0;
            return false;
    }

    public void setTitle(String title) {
            this.title = title;
    }

    public String getTitle() {
            return title;
    }

    public Object getParent() {
            return null;
    }
}
