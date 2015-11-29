// LICENSE
package com.forgedui.editor.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SWTWordWrap {
	
	// I am going to use those just for testing.
	private static Font testFontOne = new Font(null, "Serif", 4, SWT.BOLD);
	private static Font testFontTwo = new Font(null, "Serif", 10, SWT.NORMAL);
	private static Font testFontThree = new Font(null, "Serif", 20, SWT.ITALIC);

	static List<Font > fonts = new ArrayList<Font>();
	static {
		fonts.add(testFontOne);
		fonts.add(testFontTwo);
		fonts.add(testFontThree);
	}
	
	private static String testString = "Our GUI editor is a facility to create something for the mobile applications, this is a nice facility for the creation of a simple items. Well 1234567890123456789012345678901234567890 text";

	
	private static final String delimiters = " ,.!?:/*-+\n\t\r";
	
	public static List<String> wrap(String text, int width, Font font){
		if (text == null) return Collections.emptyList();
		text = text.trim();
		List<String> lines = new ArrayList<String>();
		if (text.length() != 0){
			if (FigureUtilities.getStringExtents(text, font).width > width){
				int i = text.length() - 2;
				//find the maximum string length
				for (;FigureUtilities.getStringExtents(text.substring(0, i), font).width > width
					&& i > 0;i--){
					i--;
				}
				int j = i + 1;//include previous char to delimiter test
				while (delimiters.indexOf(text.charAt(j)) < 0 && j > 0){j--;}
				if (j == 0){//no delimiters are found
					lines.add(text.substring(0, i));
					lines.addAll(wrap(text.substring(i), width, font));
				} else {//delimiter at position j
					//include delimiter as it can be not a whitespace
					lines.add(text.substring(0, j + 1).trim());
					lines.addAll(wrap(text.substring(j + 1), width, font));
				}
			} else {
				lines.add(text);
			}
		}
		return lines;
	}
	
	// Just for testing.
	public static void main(String args[]) {

		for (Font font : fonts) {
			System.out.println("----------Test font : "+ font.getFontData()[0].getName());
			List<String> items = wrap(testString, 100, font);
			for (int i = 0; i< items.size();i++) { 
				System.out.println(items.get(i));
			}
			System.out.println("-------- Done testing it ----------");
		}
	}

}
