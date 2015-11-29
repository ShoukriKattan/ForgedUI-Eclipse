//LICENSE
package com.forgedui.editor.edit.policy;

import org.eclipse.gef.editpolicies.AbstractEditPolicy;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * This class is used to remove parent EditPolicy.
 */
public class NullEditPolicy extends AbstractEditPolicy {
	
	private static NullEditPolicy INSTANCE = new NullEditPolicy();
	
	public static NullEditPolicy getInstance(){
		return INSTANCE;
	}
	
	private NullEditPolicy(){}

}
