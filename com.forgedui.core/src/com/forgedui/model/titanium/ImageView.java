/**
 *
 */
package com.forgedui.model.titanium;

import com.forgedui.model.titanium.annotations.Review;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @AutoGenerated
 */
public class ImageView extends TitaniumUIElement {

	@Unmapped
	private static final long serialVersionUID = -1577930081157584456L;
	@Unmapped
	public static final String PROP_IMAGE = "image";
	@Unmapped
	public static final String PROP_DEFAULT_IMAGE = "defaultImage";
	@Unmapped
	public static final String PROP_CAN_SCALE = "canScale";
	@Unmapped
	public static final String IMAGES_PROP = "images";

	// @Unmapped
	// public static final String PROP_URL = "url";

	@SupportedPlatform(platforms = { "android" })
	private Boolean enableZoomControls;

	@SupportedPlatform(platforms = { "android" })
	private Float duration;

	private Boolean animating;

	private Boolean reverse;

	private Boolean preventDefaultImage;

	private Boolean paused;

	@Review(note = "What is this ? it can be an array of pretty much anything. should we have represtntation")
	private String[] images;

	private String defaultImage;

	@Review(note = "It can be anything")
	private String image;

	private Integer repeatCount;

	@SupportedPlatform(platforms = "android")
	private Boolean canScale;

	// @Review(note = "This property is deprecated. use image instead")
	// private String url;

	public ImageView() {
		type = "Titanium.UI.ImageView";
	}

	@Override
	protected boolean isImageFieldName(String fieldName) {
		if (PROP_DEFAULT_IMAGE.equals(fieldName)) {
			return true;
		}
		return super.isImageFieldName(fieldName);
	}

	protected boolean isCombinedImageFieldName(String fieldName) {
		if (PROP_IMAGE.equals(fieldName))
			return true;
		return super.isCombinedImageFieldName(fieldName);
	}

	/*
	 * public String getUrl() { return url; }
	 * 
	 * public void setUrl(String url) { String oldValue = this.url; this.url =
	 * url; listeners.firePropertyChange(PROP_URL, oldValue, url); }
	 */

	/**
	 * @return the enableZoomControls
	 */
	public Boolean getEnableZoomControls() {
		return enableZoomControls;
	}

	/**
	 * @param enableZoomControls
	 *            the enableZoomControls to set
	 */
	public void setEnableZoomControls(Boolean enableZoomControls) {
		this.enableZoomControls = enableZoomControls;
	}

	/**
	 * @return the duration
	 */
	public Float getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(Float duration) {
		this.duration = duration;
	}

	/**
	 * @return the animating
	 */
	public Boolean getAnimating() {
		return animating;
	}

	/**
	 * @param animating
	 *            the animating to set
	 */
	public void setAnimating(Boolean animating) {
		this.animating = animating;
	}

	/**
	 * @return the reverse
	 */
	public Boolean getReverse() {
		return reverse;
	}

	/**
	 * @param reverse
	 *            the reverse to set
	 */
	public void setReverse(Boolean reverse) {
		this.reverse = reverse;
	}

	/**
	 * @return the preventDefaultImage
	 */
	public Boolean getPreventDefaultImage() {
		return preventDefaultImage;
	}

	/**
	 * @param preventDefaultImage
	 *            the preventDefaultImage to set
	 */
	public void setPreventDefaultImage(Boolean preventDefaultImage) {
		this.preventDefaultImage = preventDefaultImage;
	}

	/**
	 * @return the paused
	 */
	public Boolean getPaused() {
		return paused;
	}

	/**
	 * @param paused
	 *            the paused to set
	 */
	public void setPaused(Boolean paused) {
		this.paused = paused;
	}

	/**
	 * @return the images
	 */
	public String[] getImages() {
		return images;
	}

	/**
	 * @param images
	 *            the images to set
	 */
	public void setImages(String[] images) {
		this.images = images;
	}

	/**
	 * @return the defaultImage
	 */
	public String getDefaultImage() {
		return defaultImage;
	}

	/**
	 * @param defaultImage
	 *            the defaultImage to set
	 */
	public void setDefaultImage(String defaultImage) {
		String oldValue = this.defaultImage;
		this.defaultImage = defaultImage;
		listeners.firePropertyChange(PROP_DEFAULT_IMAGE, oldValue, defaultImage);
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		String oldImage = this.image;
		this.image = image;
		listeners.firePropertyChange(PROP_IMAGE, oldImage, image);

	}

	/**
	 * @return the repeatCount
	 */
	public Integer getRepeatCount() {
		return repeatCount;
	}

	/**
	 * @param repeatCount
	 *            the repeatCount to set
	 */
	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	/**
	 * @return the canScale
	 */
	public Boolean getCanScale() {
		return canScale;
	}

	/**
	 * @param canScale
	 *            the canScale to set
	 */
	public void setCanScale(Boolean canScale) {
		Boolean oldValue = this.canScale;
		this.canScale = canScale;
		listeners.firePropertyChange(PROP_CAN_SCALE, oldValue, canScale);
	}

}