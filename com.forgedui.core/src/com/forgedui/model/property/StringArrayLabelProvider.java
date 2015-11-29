package com.forgedui.model.property;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class StringArrayLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element.getClass().isArray()){
			StringBuilder sb = new StringBuilder();
			Object[] array = (Object[]) element;
			for (Object object : array) {
				sb.append(object);
				sb.append(", ");
			}
			if (array.length > 0){
				sb.delete(sb.length() - 2, sb.length() - 1);
			}
			return sb.toString();
		}
		return super.getText(element);
	}
}
