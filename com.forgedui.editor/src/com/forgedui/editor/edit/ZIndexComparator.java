// LICENSE
package com.forgedui.editor.edit;

import java.util.Comparator;

import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.util.Utils;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ZIndexComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		int zIndex1 = 0, zIndex2 = 0;
		if (o1 instanceof TitaniumUIElement
				&& ((TitaniumUIElement)o1).getZIndex() != null){
			zIndex1 = ((TitaniumUIElement)o1).getZIndex();
		} else if (o1 instanceof Picker
				&& ((Picker)o1).isExpanded()){
			zIndex1 = Integer.MAX_VALUE;
		}
		if (o2 instanceof TitaniumUIElement
				&& ((TitaniumUIElement)o2).getZIndex() != null){
			zIndex2 = ((TitaniumUIElement)o2).getZIndex();
		} else if (o2 instanceof Picker
				&& ((Picker)o2).isExpanded()){
			zIndex2 = Integer.MAX_VALUE;
		}
		return zIndex1 == zIndex2 ? 0 :
			zIndex1 > zIndex2 ? 1 : -1;
	}

}
