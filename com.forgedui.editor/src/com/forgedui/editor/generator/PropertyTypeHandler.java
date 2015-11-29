/**
 * 
 */
package com.forgedui.editor.generator;

import java.lang.reflect.Field;

/**
 * @author shoukry
 *
 */
public interface PropertyTypeHandler {

	public void handleString(String propertyName, String propertyValue, Field f);
	public void handleInteger (String properyName,String propertyValue, Field f);
}
