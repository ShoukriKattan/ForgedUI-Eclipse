// LICENSE
package com.forgedui.model.titanium;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public enum Platform {
	iPad,
	iPhone,
	Android;
	
	public boolean isIPad(){
		return this == iPad;
	}
	
	public boolean isIPhone(){
		return this == iPhone;
	}
	
	public boolean isAndroid(){
		return this == Android;
	}
	
	public static String[] getPlatforms(){
		String[] platforms = new String[values().length];
		for (int i = 0; i < values().length; i++) {
			platforms[i] = values()[i].toString();
		}
		return platforms;
	}
	
}
