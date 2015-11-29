/**
 * 
 */
package com.forgedui.model.titanium.properties;

import java.io.Serializable;

import com.forgedui.model.titanium.annotations.Unmapped;
import com.forgedui.util.Utils;

/**
 * @author shoukry
 * 
 */
public class Font implements Serializable {
		
	/**
	 * 
	 */
	@Unmapped
	private static final long serialVersionUID = 1L;
	@Unmapped
	public static final String STYLE_NORMAL = "Normal";
	@Unmapped
	public static final String WEIGHT_BOLD = "Bold";
	@Unmapped
	public static final String STYLE_ITALIC = "Italic";
	
	/*
	 * font-family string the font family font-size string the font size
	 * font-style string the font style, either normal or italics font-weight
	 * string the font weight, either normal or bold
	 */

	private String family;
	private String size;
	private String style;
	private String weight;
	
	public void init(Font parent){
		setFamily(parent.getFamily());
		setSize(parent.getSize());
		setStyle(parent.getStyle());
		setWeight(parent.getWeight());
	}
	
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((family == null) ? 0 : family.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((style == null) ? 0 : style.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Font other = (Font) obj;
		return Utils.safeEquals(family, other.family)
			&&  Utils.safeEquals(size, other.size)
			&&  Utils.safeEquals(style, other.style)
			&&  Utils.safeEquals(weight, other.weight);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(family != null ? family : "");
		sb.append('|');
		sb.append(size != null ? size : "");
		sb.append('|');
		sb.append(style != null ? style : STYLE_NORMAL);
		sb.append('|');
		sb.append(weight != null ? weight : STYLE_NORMAL);
		return sb.toString();
	}
	
}
