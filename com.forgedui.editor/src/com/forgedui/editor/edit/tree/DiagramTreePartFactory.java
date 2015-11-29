// LICENSE
package com.forgedui.editor.edit.tree;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.forgedui.model.Container;
import com.forgedui.model.Diagram;
import com.forgedui.model.Element;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.TabGroup;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TextArea;
import com.forgedui.model.titanium.TextField;
import com.forgedui.model.titanium.TitaniumUIContainer;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Window;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DiagramTreePartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof Diagram) {
			return new DiagramTreePart((Diagram) model);
		} else if (model instanceof Screen) { 
			return new ScreenTreeEditPart((Screen)model);
		} else if (model instanceof Window) {
			return new WindowTreeEditPart((Window) model);
		} else if (model instanceof TabGroup){
			return new TabGroupTreeEditPart((TabGroup)model);
		} else if (model instanceof TableView){
			return new TableViewTreeEditPart((TableView)model);
		} else if (model instanceof TableViewSection){
			return new TableViewSectionTreeEditPart((TableViewSection)model);
		} else if (model instanceof TextField){
			return new TextFieldTreeEditPart((TextField)model);
		} else if (model instanceof TextArea){
			return new TextAreaTreeEditPart((TextArea)model);
		} else if (model instanceof TitleBar){
			return new TitleBarTreeEditPart((TitleBar)model);
		} else if (model instanceof TitaniumUIContainer) {
			return new TitaniumContainerTreeEditPart((TitaniumUIContainer) model);
		} else if (model instanceof Container) {
			return new ContainerTreeEditPart((Container) model);
		} else if (model instanceof Element) {
			return new ElementTreeEditPart((Element) model);
		}
		return null;
	}

}
