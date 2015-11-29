// LICENSE
package com.forgedui.editor.edit.policy;

import org.eclipse.gef.EditPolicy;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ChangeConstraintEditPolicy extends
		AbstractConstrainedLayoutEditPolicy {
	
	public static final String KEY = EditPolicy.LAYOUT_ROLE;
	
//	/* (non-Javadoc)
//	 * @see ConstrainedLayoutEditPolicy#createChangeConstraintCommand(ChangeBoundsRequest, EditPart, Object)
//	 */
//	protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
//			EditPart child, Object constraint) {
//		if (child instanceof ElementEditPart && constraint instanceof Rectangle
//				&& !(child instanceof ScreenEditPart)
//				&& !(child instanceof WindowEditPart)
//				&& !(child instanceof TitleBarEditPart)) {
//			// return a command that can move and/or resize a Element
//			return new MoveResizeElementCommand(
//					(Element) child.getModel(), request, (Rectangle) constraint);
//		}
//		return super.createChangeConstraintCommand(request, child, constraint);
//	}

}
