/**
 * 
 */
package com.forgedui.model.titanium;

import org.eclipse.draw2d.geometry.Dimension;

import com.forgedui.model.titanium.annotations.Comments;
import com.forgedui.model.titanium.annotations.Composite;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @author shoukry
 * 
 */
@Composite
public class TitleBar extends TitaniumUIBaseElement {

	/**
	 * 
	 */
	@Unmapped
	private static final long serialVersionUID = 1L;

	@Unmapped
	public static final String PROP_TITLE = "Window.title";
	@Unmapped
	public static final String PROP_TITLE_ID = "Window.titleid";

	@Unmapped
	public static final String PROP_BAR_COLOR = "TitleBar.barColor";

	@Unmapped
	public static final String PROP_BAR_IMAGE = "TitleBar.barImage";

	@Unmapped
	public static final String PROPERTY_LEFT_NAV_BUTTON = "leftNavButton";
	
	@Unmapped
	public static final String PROPERTY_RIGHT_NAV_BUTTON = "rightNavButton";
	@Unmapped
	public static final String PROPERTY_TITLE_CONTROL = "titleControl";

	@Unmapped
	public static final String PROPERTY_TITLEPROMPT = "titlePrompt";
	@Unmapped
	public static final String PROPERTY_TITLEPROMPT_ID = "titlePromptid";

	// Those are to support the title bar color and image.
	@Unmapped
	public static final String PROPERTY_BAR_IMAGE = "barImage";
	@Unmapped
	public static final String PROPERTY_BAR_COLOR = "barColor";
	@Unmapped
	public static final String PROPERTY_TITLE_IMAGE = "titleImage";
	@Unmapped
	public static final String PROP_BACK_B_TITLE = "backButtonTitle";
	@Unmapped
	public static final String PROP_BACK_B_IMAGE = "backButtonTitleImage";

	@SupportedPlatform(platforms = "iphone")
	private String barImage;
	@SupportedPlatform(platforms = "iphone")
	private String barColor;

	private String title;
	private String titleid;
	@SupportedPlatform(platforms = "iphone")
	private String titleImage;

	@SupportedPlatform(platforms = "iphone")
	private String titlePrompt;
	@SupportedPlatform(platforms = "iphone")
	private String titlepromptid;

	@SupportedPlatform(platforms = "iphone")
	private String backButtonTitle;
	@SupportedPlatform(platforms = "iphone")
	private String backButtonTitleImage;

	@SupportedPlatform(platforms = "iphone")
	private TitaniumUIBoundedElement titleControl;

	private Button rightNavButton;

	/**
	 * the buttons are fixed in position their height and width are ignored
	 * their width is adjusted automatically according to their TEXT (length)
	 * bar height is standard and thus their max height is standard which is the
	 * bar height
	 * 
	 */
	@Comments(values = "")
	private Button leftNavButton;

	@Override
	public void setName(String name) {
		String oldName = getName();
		super.setName(name);
		if (oldName == null) {
			setTitle(name);
		}
	}

	public String getBackButtonTitleImage() {
		return backButtonTitleImage;
	}

	public void setBackButtonTitleImage(String backButtonTitleImage) {
		String oldValue = this.backButtonTitleImage;
		this.backButtonTitleImage = backButtonTitleImage;
		listeners.firePropertyChange(PROP_BACK_B_IMAGE, oldValue, backButtonTitleImage);
	}

	public Button getRightNavButton() {
		return rightNavButton;
	}

	public void setRightNavButton(Button rightNavButton) {
		Button oldRightNavButton = this.rightNavButton;
		this.rightNavButton = rightNavButton;
		fireElementPropertySet(PROPERTY_RIGHT_NAV_BUTTON, oldRightNavButton, rightNavButton);
	}

	public String getTitlePrompt() {
		return titlePrompt;
	}

	public void setTitlePrompt(String titlePrompt) {
		String oldValue = this.titlePrompt;
		this.titlePrompt = titlePrompt;
		// TODO: review this part of code - how possible to put this in
		// Controller,
		// if height depends on new and old prompt values?
		// TODO: Is possible to store and extract constants values in svg file -
		// to avoid magic numbers use?
		if ((titlePrompt == null || titlePrompt.length() == 0) && (oldValue != null && oldValue.length() > 0)) {
			Dimension dim = getDimension();
			dim.height = dim.height / 2;
			setDimension(dim);
		} else if ((oldValue == null || oldValue.length() == 0) && (titlePrompt != null && titlePrompt.length() > 0)) {
			Dimension dim = getDimension();
			dim.height = dim.height * 2;
			setDimension(dim);
		}
		//
		listeners.firePropertyChange(PROPERTY_TITLEPROMPT, oldValue, barImage);
	}

	public String getBarImage() {
		return barImage;
	}

	public void setBarImage(String barImage) {
		String oldImage = this.barImage;
		this.barImage = barImage;
		listeners.firePropertyChange(PROPERTY_BAR_IMAGE, oldImage, barImage);
	}

	public void setTitle(String title) {
		String oldTitle = this.title;
		this.title = title;
		listeners.firePropertyChange(PROP_TITLE, oldTitle, title);
	}

	public String getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(String titleImage) {
		String oldImage = this.titleImage;
		this.titleImage = titleImage;
		listeners.firePropertyChange(PROPERTY_TITLE_IMAGE, oldImage, titleImage);
	}

	public TitaniumUIBoundedElement getLeftNavButton() {
		return leftNavButton;
	}

	public void setLeftNavButton(Button leftNavButton) {
		Button oldLeftNavButton = this.leftNavButton;
		this.leftNavButton = leftNavButton;
		fireElementPropertySet(PROPERTY_LEFT_NAV_BUTTON, oldLeftNavButton, leftNavButton);
	}

	public String getTitle() {
		return title;
	}

	public String getBarColor() {
		return barColor;
	}

	public void setBarColor(String barColor) {
		String oldBarColor = this.barColor;
		this.barColor = barColor;
		listeners.firePropertyChange(PROPERTY_BAR_COLOR, oldBarColor, barColor);
	}

	public String getBackButtonTitle() {
		return backButtonTitle;
	}

	public void setBackButtonTitle(String backButtonTitle) {
		String oldValue = this.backButtonTitle;
		this.backButtonTitle = backButtonTitle;
		listeners.firePropertyChange(PROP_BACK_B_TITLE, oldValue, backButtonTitle);
	}

	public String getTitlepromptid() {
		return titlepromptid;
	}

	public void setTitlepromptid(String titlepromptid) {
		String oldValue = this.titlepromptid;
		this.titlepromptid = titlepromptid;
		listeners.firePropertyChange(PROPERTY_TITLEPROMPT_ID, oldValue, titlepromptid);
	}

	public String getTitleid() {
		return titleid;
	}

	public void setTitleid(String titleid) {
		String oldTitleid = this.titleid;
		this.titleid = titleid;
		listeners.firePropertyChange(PROP_TITLE_ID, oldTitleid, titleid);
	}

	public TitaniumUIBoundedElement getTitleControl() {
		return titleControl;
	}

	public void setTitleControl(TitaniumUIBoundedElement titleControl) {
		TitaniumUIBoundedElement oldTitleControl = this.titleControl;
		this.titleControl = titleControl;
		fireElementPropertySet(PROPERTY_TITLE_CONTROL, oldTitleControl, titleControl);
	}

	public TitleBar() {
		type = "Internal";
	}

	@Override
	protected boolean isColorFieldName(String fieldName) {
		boolean res = false;
		if (PROPERTY_BAR_COLOR.equals(fieldName)) {
			res = true;
		}
		if (!res) {
			res = super.isColorFieldName(fieldName);
		}
		return res;
	}

	@Override
	protected boolean isImageFieldName(String fieldName) {
		if (PROPERTY_BAR_IMAGE.equals(fieldName) || PROPERTY_TITLE_IMAGE.equals(fieldName)
				|| PROP_BACK_B_IMAGE.equals(fieldName)) {
			return true;
		}
		return super.isImageFieldName(fieldName);
	}

}
