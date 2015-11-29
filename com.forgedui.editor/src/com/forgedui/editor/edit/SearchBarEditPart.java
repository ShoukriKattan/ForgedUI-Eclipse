// LICENSE
package com.forgedui.editor.edit;

import com.forgedui.editor.figures.SearchBarFigure;
import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.model.titanium.SearchBar;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SearchBarEditPart extends TitaniumContainerEditPart<SearchBar> {

	@Override
	protected void refreshVisuals() {
		SearchBarFigure figure = (SearchBarFigure)getFigure();
		SearchBar model = (SearchBar)getModel();
		
		figure.setText(getHintText());
		figure.setPrompt(getPrompt());
		figure.setBarColor(model.getBarColor());
		figure.setShowCancelButton(Utils.getBoolean(model.getShowCancel(), false));
		
		super.refreshVisuals();
	}
	
	private String getHintText() {
		return ResourceHelper.getString(((SearchBar) getModel()).getHintText(),
				((SearchBar) getModel()).getHinttextid());
	}

	private String getPrompt(){
		return ResourceHelper.getString(((SearchBar) getModel()).getPrompt(),
				((SearchBar) getModel()).getPromptid());
	}

	/**
	 * Adding the direct editing manage for
	 * the search bar for the hint text.
	 */
	protected boolean directEditingEnabled() { 
		return true;
	}
	
}
