package com.forgedui.model.titanium.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * @author shoukry
 * 
 *         Specifying that a class or a property are supported by certain
 *         platforms the absence of this annotation means tht the property is
 *         supported by all platforms the presence of this property means that
 *         the property is only supported by platforms whose vales are mentioned
 *         in the annotation (platfroms property)
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedPlatform {

	String[] platforms();

}
