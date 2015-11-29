// LICENSE
package com.forgedui.editor.edit.tree;

import java.util.ArrayList;
import java.util.List;

import com.forgedui.model.titanium.TableView;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TableViewTreeEditPart extends ContainerTreeEditPart {

	/**
	 * @param c
	 */
	public TableViewTreeEditPart(TableView model) {
		super(model);
	}
	
	@Override
	public List<?> getModelChildren() {
		List<Object> list = new ArrayList<Object>(super.getModelChildren());
		TableView model = (TableView)getModel();
		if (model.getHeaderView() != null){
			list.add(model.getHeaderView());
		}
		if (model.getFooterView() != null){
			list.add(model.getFooterView());
		}
		if (model.getSearch() != null){
			list.add(model.getSearch());
		}
		return list;
	}

	@Override
	public boolean isPropertyChildName(final String name) {
		boolean res = super.isPropertyChildName(name);
		if (TableView.PROP_HEADER_VIEW.equals(name) || 
				TableView.PROP_FOOTER_VIEW.equals(name)) {
			res = true;
		}
		return res;
	}
}
