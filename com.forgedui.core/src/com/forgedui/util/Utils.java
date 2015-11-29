// LICENSE
package com.forgedui.util;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class Utils {

	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	public static boolean safeEquals(Object s1, Object s2) {
		if (s1 == null || s2 == null) {
			return s1 == null && s2 == null;
		}
		return s1.equals(s2);
	}

	public static boolean safeNotEquals(Object s1, Object s2) {
		return !safeEquals(s1, s2);
	}

	public static boolean getBoolean(Boolean b, boolean default_) {
		return b == null ? default_ : b;
	}

	public static int getInt(Integer i, int defailt_) {
		return i == null ? defailt_ : i;
	}

	public static float getFloat(Float i, float defailt_) {
		return i == null ? defailt_ : i;
	}

	public static String getString(String s, String defailt_) {
		return s == null ? defailt_ : s;
	}

	public static <T> T getObject(T t, T defailt_) {
		return t == null ? defailt_ : t;
	}

	public static boolean isNumeric(String value) {

		try {

			Double.parseDouble(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
