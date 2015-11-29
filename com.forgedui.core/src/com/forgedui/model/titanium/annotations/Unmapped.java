/**
 * 
 */
package com.forgedui.model.titanium.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author shoukry
 * Annotation specifying that this property on a model element is unmapped. 
 * Thus its an internal property which should not be mapped to js 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Unmapped {

}
