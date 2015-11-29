/**
 *
 */
package com.forgedui.model.titanium;

import com.forgedui.model.titanium.annotations.Review;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;
import com.forgedui.model.titanium.properties.ShadowOffset;

/**
 * @AutoGenerated
 */
public class Label extends TitaniumUIElement {

	/**
	 * 
	 */
	@Unmapped
	private static final long serialVersionUID = 1L;

	@Unmapped
	public static final String TEXT_PROP = "Label.text";
	@Unmapped
	public static final String TEXT_ID_PROP = "Label.textid";
	@Unmapped
	public static final String PROP_COLOR = "color";
	@Unmapped
	public static final String PROP_SHADOW_COLOR = "shadowColor";
	@Unmapped
	public static final String PROP_HIGHLIGHTED_COLOR = "highlightedColor";

	@Unmapped
	public static final String PROP_BACKGROUNDPADDING_RIGHT = "backgroundPaddingRight";
	@Unmapped
	public static final String PROP_BACKGROUNDPADDING_LEFT = "backgroundPaddingLeft";
	@Unmapped
	public static final String PROP_BACKGROUNDPADDING_BOTTOM = "backgroundPaddingBottom";
	@Unmapped
	public static final String PROP_BACKGROUNDPADDING_TOP = "backgroundPaddingTop";
	@Unmapped
	public static final String PROP_ELLIPSIZE = "ellipsize";
	@Unmapped
	public static final String PROP_TEXT_ALIGN = "textAlign";
	@Unmapped
	public static final String PROP_WORD_WRAP = "wordWrap";
	@Unmapped
	public static final String PROP_AUTO_LINK = "autoLink";

	private String text;

	private Integer backgroundPaddingRight;

	@Review(note = "What is this")
	private ShadowOffset shadowOffset;

	private String highlightedColor;

	private Boolean wordWrap;

	private Integer backgroundPaddingLeft;

	private Integer backgroundPaddingBottom;

	private Integer minimumFontSize;

	private String shadowColor;

	private String textid;

	private Integer backgroundPaddingTop;

	private String color;

	@SupportedPlatform(platforms = "android")
	private String html;

	private Alignments textAlign;

	@SupportedPlatform(platforms = "android")
	private AutoLink autoLink;

	private Boolean ellipsize;

	public Label() {
		type = "Titanium.UI.Label";
	}

	@Override
	public void setName(String name) {
		String oldName = getName();
		super.setName(name);
		if (oldName == null) {
			setText(name);
		}
	}

	public void setText(String text) {
		String oldText = this.text;
		this.text = text;
		listeners.firePropertyChange(TEXT_PROP, oldText, text);
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setting the color value of this button.
	 * 
	 * @param color
	 */
	public void setColor(String color) {
		String oldColor = this.color;
		this.color = color;
		listeners.firePropertyChange(PROP_COLOR, oldColor, color);
	}

	public String getColor() {
		return this.color;
	}

	public String getHighlightedColor() {
		return highlightedColor;
	}

	public void setHighlightedColor(String highlightedColor) {
		String oldColor = this.highlightedColor;
		this.highlightedColor = highlightedColor;
		listeners.firePropertyChange(PROP_HIGHLIGHTED_COLOR, oldColor, highlightedColor);
	}

	@Override
	protected boolean isColorFieldName(String fieldName) {
		boolean res = false;
		if (PROP_COLOR.equals(fieldName) || PROP_HIGHLIGHTED_COLOR.equals(fieldName)
				|| PROP_SHADOW_COLOR.equals(fieldName)) {
			res = true;
		}
		if (!res) {
			res = super.isColorFieldName(fieldName);
		}
		return res;
	}

	/**
	 * @return the backgroundPaddingRight
	 */
	public Integer getBackgroundPaddingRight() {
		return backgroundPaddingRight;
	}

	/**
	 * @param backgroundPaddingRight
	 *            the backgroundPaddingRight to set
	 */
	public void setBackgroundPaddingRight(Integer backgroundPaddingRight) {
		Integer oldValue = this.backgroundPaddingRight;
		this.backgroundPaddingRight = backgroundPaddingRight;
		listeners.firePropertyChange(PROP_BACKGROUNDPADDING_RIGHT, oldValue, backgroundPaddingRight);
	}

	/**
	 * @return the wordWrap
	 */
	public Boolean getWordWrap() {
		return wordWrap;
	}

	/**
	 * @param wordWrap
	 *            the wordWrap to set
	 */
	public void setWordWrap(Boolean wordWrap) {
		Boolean oldValue = this.wordWrap;
		this.wordWrap = wordWrap;
		listeners.firePropertyChange(PROP_WORD_WRAP, oldValue, wordWrap);
	}

	/**
	 * @return the backgroundPaddingLeft
	 */
	public Integer getBackgroundPaddingLeft() {
		return backgroundPaddingLeft;
	}

	/**
	 * @param backgroundPaddingLeft
	 *            the backgroundPaddingLeft to set
	 */
	public void setBackgroundPaddingLeft(Integer backgroundPaddingLeft) {
		Integer oldValue = this.backgroundPaddingLeft;
		this.backgroundPaddingLeft = backgroundPaddingLeft;
		listeners.firePropertyChange(PROP_BACKGROUNDPADDING_LEFT, oldValue, backgroundPaddingLeft);
	}

	/**
	 * @return the backgroundPaddingBottom
	 */
	public Integer getBackgroundPaddingBottom() {
		return backgroundPaddingBottom;
	}

	/**
	 * @param backgroundPaddingBottom
	 *            the backgroundPaddingBottom to set
	 */
	public void setBackgroundPaddingBottom(Integer backgroundPaddingBottom) {
		Integer oldValue = this.backgroundPaddingBottom;
		this.backgroundPaddingBottom = backgroundPaddingBottom;
		listeners.firePropertyChange(PROP_BACKGROUNDPADDING_BOTTOM, oldValue, backgroundPaddingBottom);
	}

	/**
	 * @return the minimumFontSize
	 */
	public Integer getMinimumFontSize() {
		return minimumFontSize;
	}

	/**
	 * @param minimumFontSize
	 *            the minimumFontSize to set
	 */
	public void setMinimumFontSize(Integer minimumFontSize) {
		this.minimumFontSize = minimumFontSize;
	}

	/**
	 * @return the shadowColor
	 */
	public String getShadowColor() {
		return shadowColor;
	}

	/**
	 * @param shadowColor
	 *            the shadowColor to set
	 */
	public void setShadowColor(String shadowColor) {
		this.shadowColor = shadowColor;
	}

	/**
	 * @return the textid
	 */
	public String getTextid() {
		return textid;
	}

	/**
	 * @param textid
	 *            the textid to set
	 */
	public void setTextid(String textid) {
		String old = this.textid;
		this.textid = textid;
		listeners.firePropertyChange(TEXT_ID_PROP, old, textid);
	}

	/**
	 * @return the backgroundPaddingTop
	 */
	public Integer getBackgroundPaddingTop() {
		return backgroundPaddingTop;
	}

	/**
	 * @param backgroundPaddingTop
	 *            the backgroundPaddingTop to set
	 */
	public void setBackgroundPaddingTop(Integer backgroundPaddingTop) {
		Integer oldValue = this.backgroundPaddingTop;
		this.backgroundPaddingTop = backgroundPaddingTop;
		listeners.firePropertyChange(PROP_BACKGROUNDPADDING_TOP, oldValue, backgroundPaddingTop);
	}

	/**
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}

	/**
	 * @param html
	 *            the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/**
	 * @return the textAlign
	 */
	public Alignments getTextAlign() {
		return textAlign;
	}

	/**
	 * @param textAlign
	 *            the textAlign to set
	 */
	public void setTextAlign(Alignments textAlign) {
		Alignments oldValue = this.textAlign;
		this.textAlign = textAlign;
		listeners.firePropertyChange(PROP_TEXT_ALIGN, oldValue, textAlign);
	}

	/**
	 * @return the autoLink
	 */
	public AutoLink getAutoLink() {
		return autoLink;
	}

	/**
	 * @param autoLink
	 *            the autoLink to set
	 */
	public void setAutoLink(AutoLink autoLink) {
		AutoLink oldValue = this.autoLink;
		this.autoLink = autoLink;
		listeners.firePropertyChange(PROP_AUTO_LINK, oldValue, autoLink);
	}

	/**
	 * @return the ellipsize
	 */
	public Boolean getEllipsize() {
		return ellipsize;
	}

	/**
	 * @param ellipsize
	 *            the ellipsize to set
	 */
	public void setEllipsize(Boolean ellipsize) {
		Boolean oldValue = this.ellipsize;
		this.ellipsize = ellipsize;
		listeners.firePropertyChange(PROP_ELLIPSIZE, oldValue, ellipsize);
	}

	/**
	 * For the alignment of the items.
	 */
	public static enum Alignments implements EnumType{
		TEXT_ALIGNMENT_LEFT,TEXT_ALIGNMENT_CENTER,TEXT_ALIGNMENT_RIGHT;
		
		public String getQDN() {
			return "Titanium.UI." + super.toString();
		};
	}

	/**
	 * Those are newlly added for the auto links.
	 * 
	 * @author Tareq Doufish
	 * 
	 */
	public static enum AutoLink implements EnumType{
		LINKIFY_ALL, LINKIFY_EMAIL_ADDRESSES, LINKIFY_MAP_ADDRESSES, LINKIFY_MAP_LINKS, LINKIFY_PHONE_NUMBERS, LINKIFY_WEB_URLS;
		
		public String getQDN() {
			return "Titanium.UI.Android." + super.toString();
		};
	}
}