/**
 * 
 */
package com.forgedui.model.titanium.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author shoukry
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatValueRange {

	float to() default 0;

	float from() default 0;
	
	//"minonly" or "maxonly"
	String type() default "";

}

