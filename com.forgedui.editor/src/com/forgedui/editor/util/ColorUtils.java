// LICENSE
package com.forgedui.editor.util;

import org.eclipse.swt.graphics.RGB;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ColorUtils {
	
	public static RGB lighter(RGB rgb){
		return lighter(rgb, 20);
	}
	
	public static RGB darker(RGB rgb){
		return darker(rgb, 85);
	}
	
	public static RGB lighter(RGB rgb, int percent){
		return new RGB(
				rgb.red + (255 - rgb.red)*percent/100,
				rgb.green + (255 - rgb.green)*percent/100,
				rgb.blue + (255 - rgb.blue)*percent/100);
	}
	
	public static RGB darker(RGB rgb, int percent){
		return new RGB(
				rgb.red*percent/100,
				rgb.green*percent/100,
				rgb.blue*percent/100);
	}

}
