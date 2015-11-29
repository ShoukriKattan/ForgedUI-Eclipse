/**
 * 
 */
package com.forgedui.model.titanium;

import org.eclipse.draw2d.geometry.Dimension;

import com.forgedui.model.titanium.annotations.FloatValueRange;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;
import com.forgedui.model.titanium.properties.AnchorPoint;
import com.forgedui.model.titanium.properties.AnimatedCenterPoint;
import com.forgedui.model.titanium.properties.BackgroundGradient;
import com.forgedui.model.titanium.properties.Font;
import com.forgedui.model.titanium.properties.Size;
import com.forgedui.model.titanium.properties.Transform;

/**
 * @author shoukry
 * 
 *         In general Element and Container are generic for this editor if we
 *         want to re-use the editor for other technologies then we have to
 *         extend element
 * 
 *         TitaniumUIElement extends the element to provide for properties
 *         specific for Titanium UI elements
 * 
 * 
 */
public abstract class TitaniumUIElement extends TitaniumUIBoundedElement {

	@Unmapped
	public static final String PROPERTY_VISIBLE = "visible";

	// Those are constants for the bar UI element.
	@Unmapped
	public static final String PROPERTY_BORDER_RADIUS = "borderRadius";
	@Unmapped
	public static final String PROPERTY_BORDER_COLOR = "borderColor";
	@Unmapped
	public static final String PROPERTY_BORDER_WIDTH = "borderWidth";
	@Unmapped
	public static final String PROPERTY_OPACITY = "opacity";
	@Unmapped
	public static final String PROPERTY_BACKGROUND_SELECTED_COLOR = "backgroundSelectedColor";
	@Unmapped
	public static final String PROPERTY_BACKGROUND_FOCUSED_COLOR = "backgroundFocusedColor";
	@Unmapped
	public static final String PROPERTY_BACKGROUND_DISABLED_COLOR = "backgroundDisabledColor";
	@Unmapped
	public static final String PROPERTY_BACKGROUND_COLOR = "backgroundColor";
	@Unmapped
	public static final String PROPERTY_BACKGROUND_IMAGE = "backgroundImage";
	@Unmapped
	public static final String PROPERTY_BACKGROUND_SELECTED_IMAGE = "backgroundSelectedImage";
	@Unmapped
	public static final String PROPERTY_BACKGROUND_DISABLED_IMAGE = "backgroundDisabledImage";
	@Unmapped
	public static final String PROPERTY_BACKGROUND_FOCUSED_IMAGE = "backgroundFocusedImage";
	@Unmapped
	public static final String PROPERTY_FONT = "font";
	@Unmapped
	public static final String PROPERTY_ZINDEX = "zIndex";

	/**
	 * 
	 */
	@Unmapped
	private static final long serialVersionUID = -1577930081157584455L;

	/**
	 * object a dictionary with properties x and y to indicate the anchor point
	 * value. anchor specifies the position by which animation should occur.
	 * center is 0.5, 0.5
	 */
	protected AnchorPoint anchorPoint;

	/**
	 * object read-only object with x and y properties of where the view is
	 * during animation
	 */
	protected AnimatedCenterPoint animatedCenterPoint;

	//
	//
	protected String backgroundColor;
	/**
	 * The disabled background color of the view. (Android)
	 * 
	 */
	@SupportedPlatform(platforms = "android")
	protected String backgroundDisabledColor;

	/**
	 * url to an image that is drawn as the background of the button when the
	 * button is in the disabled state
	 */
	@SupportedPlatform(platforms = "android")
	protected String backgroundDisabledImage;

	/**
	 * the focused background color of the view. focusable must be true for
	 * normal views. (Android)
	 */
	@SupportedPlatform(platforms = "android")
	protected String backgroundFocusedColor;

	/**
	 * The focused background image url of the view. focusable must be true for
	 * normal views. (Android)
	 */

	@SupportedPlatform(platforms = { "android" })
	protected String backgroundFocusedImage;

	/**
	 * object a background gradient for the view with the properties:
	 * type,startPoint
	 * ,endPoint,startRadius,endRadius,backfillStart,backfillEnd,colors.
	 */
	protected BackgroundGradient backgroundGradient;

	/**
	 * TODO: is specific ? url to a button image that is drawn as the background
	 * of the button
	 */
	protected String backgroundImage;

	/**
	 * End caps specify the portion of an image that should not be resized when
	 * an image is stretched. This technique is used to implement buttons and
	 * other resizable image-based interface elements. When a button with end
	 * caps is resized, the resizing occurs only in the middle of the button, in
	 * the region between the end caps. The end caps themselves keep their
	 * original size and appearance. This property specifies the size of the
	 * left end cap. The middle (stretchable) portion is assumed to be 1 pixel
	 * wide. The right end cap is therefore computed by adding the size of the
	 * left end cap and the middle portion together and then subtracting that
	 * value from the width of the image
	 */
	@SupportedPlatform(platforms = { "iphone" })
	protected Float backgroundLeftCap;

	/**
	 * the selected background color of the view. focusable must be true for
	 * normal views. (Android)
	 */
	@SupportedPlatform(platforms = { "android" })
	protected String backgroundSelectedColor;

	/**
	 * TODO: is specific? url to a button image that is drawn as the background
	 * of the button when the button is in the selected state
	 */
	protected String backgroundSelectedImage;
	/**
	 * End caps specify the portion of an image that should not be resized when
	 * an image is stretched. This technique is used to implement buttons and
	 * other resizable image-based interface elements. When a button with end
	 * caps is resized, the resizing occurs only in the middle of the button, in
	 * the region between the end caps. The end caps themselves keep their
	 * original size and appearance. This property specifies the size of the top
	 * end cap. The middle (stretchable) portion is assumed to be 1 pixel wide.
	 * The sBottom end cap is therefore computed by adding the size of the top
	 * end cap and the middle portion together and then subtracting that value
	 * from the height of the image
	 */
	@SupportedPlatform(platforms = { "iphone" })
	protected String backgroundTopCap;

	/**
	 * the border color of the view
	 */
	protected String borderColor;

	/**
	 * the border radius of the view
	 */
	protected Float borderRadius;

	/**
	 * the border width of the view
	 */
	protected Float borderWidth;

	/**
	 * boolean Set true if you want a view to be focusable when navigating with
	 * the trackball or D-Pad. Default: false. (Android Only)
	 */
	@SupportedPlatform(platforms = "android")
	protected Boolean focusable;

	/**
	 * the font properties of the button
	 */
	protected Font font;

	/*
	 * font-family string the font family font-size string the font size
	 * font-style string the font style, either normal or italics font-weight
	 * string the font weight, either normal or bold
	 * 
	 * string property for the view height. Can be either a float value or a
	 * dimension string ie 'auto' (default), '50%' (iPhone only).
	 */

	/**
	 * the opacity from 0.0-1.0
	 */
	@FloatValueRange(from = 0.0f, to = 1.0f)
	protected Float opacity;

	/**
	 * object the size of the view as a dictionary of width and height
	 * properties
	 */
	protected Size size;

	/**
	 * TODO: best implementation? int One of
	 * Titanium.UI.Android.SOFT_KEYBOARD_DEFAULT_ON_FOCUS,
	 * Titanium.UI.Android.SOFT_KEYBOARD_HIDE_ON_FOCUS, or
	 * Titanium.UI.Android.SOFT_KEYBOARD_SHOW_ON_FOCUS. (Android only)
	 */
	@SupportedPlatform(platforms = "android")
	protected SoftKeyboardType softKeyboardOnFocus;

	/**
	 * TODO: specific? boolean indicating if the view should receive touch
	 * events (true, default) or forward them to peers (false)
	 */
	protected Boolean touchEnabled;

	/**
	 * object the transformation matrix to apply to the view
	 */
	protected Transform transform;

	/**
	 * boolean a boolean of the visibility of the view
	 */
	protected Boolean visible;

	/**
	 * int the z index position relative to other sibling views
	 */

	protected Integer zIndex;

	/*
	 * @Override public void setDimension(Dimension defSize) { //update top and
	 * left if they are not null, but width and height are null! if (getWidth()
	 * == null && getParent() != null && getLeft() != null && getRight() !=
	 * null){ setRight(getParent().getDimension().width - getLeft() -
	 * defSize.width); } else { setWidth(defSize.width); }
	 * 
	 * if (getHeight() == null && getParent() != null && getTop() != null &&
	 * getBottom() != null){ setBottom(getParent().getDimension().height -
	 * getTop() - defSize.height); } else { setHeight(defSize.height); }
	 * 
	 * listeners.firePropertyChange(PROPERTY_SIZE, null, getDimension()); }
	 * 
	 * @Override public void setLocation(Point location) { //update sBottom and
	 * right if they are not null, but top and left are null! if (getLeft() ==
	 * null && getParent() != null && getWidth() != null && getRight() != null){
	 * setRight(getParent().getDimension().width - getWidth() - location.x); }
	 * else { setLeft(location.x); }
	 * 
	 * if (getTop() == null && getParent() != null && getHeight() != null &&
	 * getBottom() != null){ setBottom(getParent().getDimension().height -
	 * getHeight() - location.y); } else { setTop(location.y); }
	 * 
	 * //listeners.firePropertyChange(PROPERTY_LOCATION, null, getLocation()); }
	 */

	public AnchorPoint getAnchorPoint() {
		return anchorPoint;
	}

	public void setAnchorPoint(AnchorPoint anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	public AnimatedCenterPoint getAnimatedCenterPoint() {
		return animatedCenterPoint;
	}

	public void setAnimatedCenterPoint(AnimatedCenterPoint animatedCenterPoint) {
		this.animatedCenterPoint = animatedCenterPoint;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		String oldColor = this.backgroundColor;
		this.backgroundColor = backgroundColor;
		listeners.firePropertyChange(PROPERTY_BACKGROUND_COLOR, oldColor, this.backgroundColor);
	}

	public String getBackgroundDisabledColor() {
		return backgroundDisabledColor;
	}

	public void setBackgroundDisabledColor(String backgroundDisabledColor) {
		String oldColor = this.backgroundDisabledColor;
		this.backgroundDisabledColor = backgroundDisabledColor;
		listeners.firePropertyChange(PROPERTY_BACKGROUND_DISABLED_COLOR, oldColor, backgroundDisabledColor);
	}

	public String getBackgroundDisabledImage() {
		return backgroundDisabledImage;
	}

	public void setBackgroundDisabledImage(String backgroundDisabledImage) {
		String oldImage = this.backgroundDisabledImage;
		this.backgroundDisabledImage = backgroundDisabledImage;
		listeners.firePropertyChange(PROPERTY_BACKGROUND_DISABLED_IMAGE, oldImage, backgroundDisabledImage);
	}

	public String getBackgroundFocusedColor() {
		return backgroundFocusedColor;
	}

	public void setBackgroundFocusedColor(String backgroundFocusedColor) {
		this.backgroundFocusedColor = backgroundFocusedColor;
	}

	public String getBackgroundFocusedImage() {
		return backgroundFocusedImage;
	}

	public void setBackgroundFocusedImage(String backgroundFocusedImage) {
		this.backgroundFocusedImage = backgroundFocusedImage;
	}

	public BackgroundGradient getBackgroundGradient() {
		return backgroundGradient;
	}

	public void setBackgroundGradient(BackgroundGradient backgroundGradient) {
		this.backgroundGradient = backgroundGradient;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		String oldImage = this.backgroundImage;
		this.backgroundImage = backgroundImage;
		listeners.firePropertyChange(PROPERTY_BACKGROUND_IMAGE, oldImage, backgroundImage);
	}

	public Float getBackgroundLeftCap() {
		return backgroundLeftCap;
	}

	public void setBackgroundLeftCap(Float backgroundLeftCap) {
		this.backgroundLeftCap = backgroundLeftCap;
	}

	public String getBackgroundSelectedColor() {
		return backgroundSelectedColor;
	}

	public void setBackgroundSelectedColor(String backgroundSelectedColor) {
		String oldColor = this.backgroundSelectedColor;
		this.backgroundSelectedColor = backgroundSelectedColor;
		listeners.firePropertyChange(PROPERTY_BACKGROUND_SELECTED_COLOR, oldColor, backgroundSelectedColor);
	}

	public String getBackgroundSelectedImage() {
		return backgroundSelectedImage;
	}

	public void setBackgroundSelectedImage(String backgroundSelectedImage) {
		String oldImage = this.backgroundSelectedImage;
		this.backgroundSelectedImage = backgroundSelectedImage;
		listeners.firePropertyChange(PROPERTY_BACKGROUND_SELECTED_IMAGE, oldImage, backgroundSelectedImage);
	}

	public String getBackgroundTopCap() {
		return backgroundTopCap;
	}

	public void setBackgroundTopCap(String backgroundTopCap) {
		this.backgroundTopCap = backgroundTopCap;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		String oldValue = this.borderColor;
		this.borderColor = borderColor;
		listeners.firePropertyChange(PROPERTY_BORDER_COLOR, oldValue, this.borderColor);
	}

	public Float getBorderRadius() {
		return borderRadius;
	}

	public void setBorderRadius(Float borderRadius) {
		Float oldValue = this.borderRadius;
		this.borderRadius = borderRadius;
		listeners.firePropertyChange(PROPERTY_BORDER_RADIUS, oldValue, this.borderRadius);
	}

	public Float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(Float borderWidth) {
		Float oldValue = this.borderWidth;
		this.borderWidth = borderWidth;
		listeners.firePropertyChange(PROPERTY_BORDER_WIDTH, oldValue, this.borderWidth);
	}

	public Boolean getFocusable() {
		return focusable;
	}

	public void setFocusable(Boolean focusable) {
		this.focusable = focusable;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		Font old = this.font;
		this.font = font;
		listeners.firePropertyChange(PROPERTY_FONT, old, font);
	}

	public Float getOpacity() {
		return opacity;
	}

	public void setOpacity(Float opacity) {
		Float oldValue = this.opacity;
		this.opacity = opacity;
		listeners.firePropertyChange(PROPERTY_OPACITY, oldValue, opacity);
	}

	/**
	 * TODO: Lets fix property name colllision
	 */
	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		Dimension oldDimension = getDimension();
		this.size = size;
		listeners.firePropertyChange(PROPERTY_SIZE, oldDimension, getDimension());
	}

	public SoftKeyboardType getSoftKeyboardOnFocus() {
		return softKeyboardOnFocus;
	}

	public void setSoftKeyboardOnFocus(SoftKeyboardType softKeyboardOnFocus) {
		this.softKeyboardOnFocus = softKeyboardOnFocus;
	}

	public Boolean getTouchEnabled() {
		return touchEnabled;
	}

	public void setTouchEnabled(Boolean touchEnabled) {
		this.touchEnabled = touchEnabled;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		Boolean old = this.visible;
		this.visible = visible;
		listeners.firePropertyChange(PROPERTY_VISIBLE, old, visible);
	}

	public Integer getZIndex() {
		return zIndex;
	}

	public void setZIndex(Integer zIndex) {
		Integer oldValue = this.zIndex;
		this.zIndex = zIndex;
		listeners.firePropertyChange(PROPERTY_ZINDEX, oldValue, zIndex);
	}

	protected boolean isColorFieldName(String fieldName) {
		boolean res = false;
		if (PROPERTY_BACKGROUND_COLOR.equals(fieldName) || PROPERTY_BACKGROUND_DISABLED_COLOR.equals(fieldName)
				|| PROPERTY_BACKGROUND_FOCUSED_COLOR.equals(fieldName)
				|| PROPERTY_BACKGROUND_SELECTED_COLOR.equals(fieldName) || PROPERTY_BORDER_COLOR.equals(fieldName)) {
			res = true;
		}
		return res;
	}

	protected boolean isImageFieldName(String fieldName) {
		boolean res = false;
		if (PROPERTY_BACKGROUND_IMAGE.equals(fieldName) || PROPERTY_BACKGROUND_SELECTED_IMAGE.equals(fieldName)
				|| PROPERTY_BACKGROUND_DISABLED_IMAGE.equals(fieldName)
				|| PROPERTY_BACKGROUND_FOCUSED_IMAGE.equals(fieldName)) {
			res = true;
		}
		return res;
	}

	/**
	 * Just another method to add the new property descriptor for the fields
	 * that will have the url image as well.
	 * 
	 * @param fieldName
	 * @return
	 */
	protected boolean isCombinedImageFieldName(String fieldName) {
		return false;
	}
	
	public static enum SoftKeyboardType implements EnumType{
		SOFT_KEYBOARD_DEFAULT_ON_FOCUS,
		SOFT_KEYBOARD_HIDE_ON_FOCUS,
		SOFT_KEYBOARD_SHOW_ON_FOCUS;

		@Override
		public String getQDN() {
			return "Titanium.UI.Android."+super.toString();
		}
	}
	
	public static enum KeyboardType implements EnumType{
		KEYBOARD_DEFAULT,
		KEYBOARD_EMAIL,
		KEYBOARD_ASCII,
		KEYBOARD_URL,
		KEYBOARD_NUMBER_PAD,
		KEYBOARD_NUMBER_PUNCTUATION,
		KEYBOARD_PHONE_PAD;
		
		@Override
		public String getQDN() {
			return "Titanium.UI."+super.toString();
		}
	}

	public interface EnumType{
		public String getQDN();
	}
}
