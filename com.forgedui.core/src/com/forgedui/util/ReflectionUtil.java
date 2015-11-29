/**
 * 
 */
package com.forgedui.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.forgedui.core.ForgedUICorePlugin;
import com.forgedui.model.titanium.TitaniumUIBaseElement;

/**
 * @author shoukry
 * 
 */
public class ReflectionUtil {
	
	public static boolean executeMethod(Object obj, Method m, Object... args){
		try {
			m.invoke(obj, args);
			return true;
		} catch (Exception e) {
			ForgedUICorePlugin.logError(e);
			return false;
		}
	}
	
	public static Method getSetter(Class<?> c, Field f){
		String setterName = "set" + Character.toUpperCase(f.getName().charAt(0))
			+ f.getName().substring(1);
		Method m;
		try {
			m = c.getMethod(setterName, f.getType());
			return m;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Cant find a setter method for \'"
				+ f.getName() + "\' field.");
	}

	public static List<Field> getObjectFields(Object obj,
			Class<?> highestClassInHierarchy) {

		if (highestClassInHierarchy == null) {
			highestClassInHierarchy = Object.class;

		}

		// Does the object currently implement this interface, or can be cast to
		// this class

		if (!highestClassInHierarchy.isAssignableFrom(obj.getClass())) {
			throw new IllegalArgumentException(
					"The object does not implement the highest class in the hierarchy...");
		}

		List<Field> fields = new ArrayList<Field>();

		Class<?> clazzInH = obj.getClass();

		while (true) {

			Field[] fieldz = clazzInH.getDeclaredFields();

			for (Field f : fieldz) {
				fields.add(f);
			}

			//break;
			clazzInH = (Class<?>) clazzInH.getSuperclass();

			if (!highestClassInHierarchy.isAssignableFrom(clazzInH)) {
				break;
			}

			if (clazzInH.equals(Object.class)) {

				throw new IllegalArgumentException(
						"Object's highest classInHierarchy is not correct. We got to Object.class without converging");

				// Try to see

			}
		}

		return fields;

	}

	public static void main(String[] args) {

		ReflectionUtil.getObjectFields(
				new com.forgedui.model.titanium.Window(),
				TitaniumUIBaseElement.class);
	}
}
