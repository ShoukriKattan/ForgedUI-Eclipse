// LICENSE
package com.forgedui.editor.edit.tree;

import java.util.ArrayList;
import java.util.List;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.TextArea;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TextAreaTreeEditPart extends ElementTreeEditPart {

	/**
	 * @param e
	 */
	public TextAreaTreeEditPart(Element e) {
		super(e);
	}
	
	@Override
	public List<?> getModelChildren() {
		List<Element> list = new ArrayList<Element>();
		TextArea model = (TextArea)getModel();
		if (model.getKeyboardToolbar() != null && model.getKeyboardToolbar().length > 0){
			for (int i = 0 ; i < model.getKeyboardToolbar().length ; i++) { 
				list.add(model.getKeyboardToolbar()[i]);
			}
		}
		return list;
	}

}
