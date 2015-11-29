// LICENSE
package com.forgedui.editor.edit.tree;

import java.util.ArrayList;
import java.util.List;

import com.forgedui.model.titanium.TableViewSection;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TableViewSectionTreeEditPart extends ContainerTreeEditPart {

	/**
	 * @param c
	 */
	public TableViewSectionTreeEditPart(TableViewSection section) {
		super(section);
	}
	
	@Override
	public List<?> getModelChildren() {
		List<Object> list = new ArrayList<Object>(super.getModelChildren());
		TableViewSection model = (TableViewSection)getModel();
		if (model.getHeaderView() != null){
			list.add(model.getHeaderView());
		}
		if (model.getFooterView() != null){
			list.add(model.getFooterView());
		}
		return list;
	}

	@Override
	public boolean isPropertyChildName(final String name) {
		boolean res = super.isPropertyChildName(name);
		if (TableViewSection.PROP_HEADER_VIEW.equals(name) || 
				TableViewSection.PROP_FOOTER_VIEW.equals(name)) {
			res = true;
		}
		return res;
	}
}
