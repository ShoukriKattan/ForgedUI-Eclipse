/**
 * 
 */
package com.forgedui.model.titanium.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author shoukry
 * 
 * Any element marked with this annotation is considered only a logical split
 * the compiler will take the attributes and attach them to the parent object directly
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Composite {

}
