package com.forgedui.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.forgedui.core.TitaniumProject;
import com.forgedui.model.Diagram;
import com.forgedui.model.titanium.properties.Font;

/**
 * This class is used for the conversion of the values between the model and
 * some swt components likes color
 * 
 * @author Tareq Doufish
 * 
 */
public class Converter {

	public static String getHexColorValue(Color color) {
		return getHexColorValue(color.getRGB());
	}

	public static String getHexColorValue(RGB color) {
		return getHexColorValue(color.red, color.green, color.blue);
	}

	/**
	 * Method to get the color value in hexa from a given swt stuff.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static String getHexColorValue(int r, int g, int b) {
		java.awt.Color color = new java.awt.Color(r, g, b);
		String hexa = Integer.toHexString(color.getRGB() & 0x00ffffff);
		return ("#" + "000000".substring(hexa.length()) + hexa.toUpperCase());
	}

	/**
	 * This will get the SWT color from a hexa string.
	 * 
	 * @param hexa
	 * @return
	 */
	public static Color getColorFromHexa(String hexa) {
		java.awt.Color otherColor = java.awt.Color.decode(hexa);
		return new Color(Display.getCurrent(), otherColor.getRed(),
				otherColor.getGreen(), otherColor.getBlue());
	}

	/**
	 * This will return an instanceo of UI font given a font data.
	 * 
	 * @param data
	 * @return
	 */
	public static Font getFontFromFontData(FontData data) {
		String name = data.getName();
		int height = data.getHeight();
		Font font = new Font();
		font.setFamily(name);
		font.setSize(height + "");
		if ((data.getStyle() & SWT.BOLD) != 0){
			font.setWeight(Font.WEIGHT_BOLD);
		} else {
			font.setWeight(null);
		}
		if ((data.getStyle() & SWT.ITALIC) != 0){
			font.setStyle(Font.STYLE_ITALIC);
		} else {
			font.setStyle(null);
		}

		return font;
	}

	public static FontData getFontDataFromFont(Font font) {
		if (font != null) {
			int size = Integer.parseInt(font.getSize());
			int style = SWT.NORMAL;
			if (Font.STYLE_ITALIC.equals(font.getStyle())){
	    		style |= SWT.ITALIC;
	    	}
			if (Font.WEIGHT_BOLD.equals(font.getWeight())){
	    		style |= SWT.BOLD;
	    	}
			return new FontData(font.getFamily(), size, style);
		}
		return null;
	}

	/**
	 * There is special way of dealing with font in regards with the font weight
	 * and the font size.
	 * 
	 * @param fontFamily
	 * @param fontSize
	 * @param fontWeight
	 * @param fontStyle
	 */
	public static java.awt.Font getAwtFontFromSVGFont(String fontFamily,
			String fontSize, String fontWeight, String fontStyle) {
		int fontWeightValue = -1;
		int fontSizeValue = 10;

		if (fontWeight != null) {
			try {
				// if it parsed well, then the font weight is a number.
				fontWeightValue = Integer.parseInt(fontWeight);
			} catch (Exception e) {
			}
		}
		// Trying to figure out the font size.
		if (fontSize != null) {
			try {
				fontSizeValue = Integer.parseInt(fontSize);
			} catch (Exception e) {
			}
		}

		int fontStyleValue = java.awt.Font.PLAIN;

		if (fontWeightValue != -1) {

		} else {
			// Dealing with it with a potential two values. normal | bold
			if (fontWeight.equalsIgnoreCase("bold")) {
				fontStyleValue = java.awt.Font.BOLD;
			}
		}

		// normal | italic | oblique << Those are the values of the font from
		// the svg document.
		if (fontStyle != null) {
			if (fontStyle.equalsIgnoreCase("italic")) {
				fontStyleValue = java.awt.Font.ITALIC;
			} else if (fontStyle.equalsIgnoreCase("oblique")) {
				// I don't know how to map this value ???!
				fontStyleValue = java.awt.Font.PLAIN;
			}
		}

		return new java.awt.Font(fontFamily, fontStyleValue, fontSizeValue);
	}

	public static int getComboValue(Object value) {
		if (value == null || value == "") {
			return 0;
		} else if (value.equals(Boolean.TRUE)) {
			return 1;
		} else if (value.equals(Boolean.FALSE)) {
			return 2;
		}
		return 0;
	}

	/**
	 * @param value
	 * @return
	 */
	public static Boolean getBooleanValue(Object value) {
		if (value != null){
			if (value.equals(new Integer(1))) {
				return new Boolean(true);
			} else if (value.equals(new Integer(2))) {
				return new Boolean(false);
			}
		}
		return null;
	}

	public static String getFullPathOfResource(String filePath) {
		int index = filePath.lastIndexOf("/");
		return filePath.substring(0, index);
	}

	public static String getImageFullPath(TitaniumProject project, String imageProperty){
		if (Utils.isNotEmpty(imageProperty)){
			if (imageProperty.startsWith("http://")){//only this protocol!!!
				return imageProperty;
			} else {
				return project.getReourcesFolder()
					.getLocation().append(imageProperty).toOSString();
			}
		}
		return null;
	}
	
	public static String getImageFullPath(Diagram diagram, String imageProperty){
		return getImageFullPath(diagram.getProject(), imageProperty);
	}

	// Thos are for validating the url ...
	private static String urlPattern = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	public static boolean isStringUrl(String url) {
		return isMatch(url, urlPattern);
	}

	private static boolean isMatch(String s, String pattern) {
		try {
			Pattern patt = Pattern.compile(pattern);
			Matcher matcher = patt.matcher(s);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}
	}

	// Added by Shoukry for convinience
	public static ImageData getImageDataFromURL(String url) {

		InputStream is = null;
		try {
			URL webUrl = new URL(url);
			is = webUrl.openStream();
			return new ImageData(is);
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	/**
	 * Creates us a swt image from the url of the web.
	 * 
	 * @param url
	 * @return
	 */
	public static Image getImageFromUrl(String url) {
		try {
			ImageLoader il = new ImageLoader();
			return new Image(null, il.load(url)[0]);
		} catch (Exception e){
		//	e.printStackTrace();
		}
		InputStream is = null;
		try {
			URL webUrl = new URL(url);
			is = webUrl.openStream();
			return new Image(Display.getCurrent(), is);
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Trying to load the image via a new thread...
	 * 
	 * @param url
	 * @param listener
	 * @return
	 */
	public static void loadImage(final String url,
			final IURLImageListener listener) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {

				InputStream is = null;
				try {
					URL webUrl = new URL(url);
					is = webUrl.openStream();
					ImageData image = new ImageData(is);
					listener.imageLoaded(image);
				} catch (Exception e) {
					e.printStackTrace();
					listener.imageFailedToLoad();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
}
