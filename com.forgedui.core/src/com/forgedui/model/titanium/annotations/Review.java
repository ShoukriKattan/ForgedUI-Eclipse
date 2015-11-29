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
@Retention(RetentionPolicy.SOURCE)
public @interface Review {

	String note() default "";

}
