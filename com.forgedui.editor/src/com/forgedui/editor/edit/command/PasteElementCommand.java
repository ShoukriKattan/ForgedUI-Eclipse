package com.forgedui.editor.edit.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.viewers.StructuredSelection;

import com.forgedui.editor.GUIEditor;
import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.TabbedBar;
import com.forgedui.model.titanium.TitaniumUIBaseElement;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;

/**
 * 
 * @author Zrieq
 *
 */
public class PasteElementCommand extends Command {
	private HashMap<Element, Element> list = new HashMap<Element, Element>();
	private AbstractEditPart pasteTarget;
	private List<EditPart> newTopLevelParts;
	private GUIEditor guiEditor;
	
	public PasteElementCommand(AbstractEditPart pasteTarget, GUIEditor guiEditor){
		this.pasteTarget = pasteTarget;
		this.guiEditor = guiEditor;
		newTopLevelParts = new ArrayList<EditPart>();
	}
	
	@Override
	public boolean canExecute() {
		ArrayList<Element> bList = (ArrayList<Element>) Clipboard.getDefault()
				.getContents();
		if (bList == null || bList.isEmpty())
			return false;
		Iterator<Element> it = bList.iterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			if (isPastableElement(element)) {
				list.put(element, null);
			}else
				return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if (!canExecute())
			return;
		Iterator<Element> it = list.keySet().iterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			Element clone = cloneElement(element,(Container)pasteTarget.getModel());
			list.put(element, clone);
			newTopLevelParts.add(findEditPartForModel(pasteTarget,clone));
		}
		guiEditor.getGraphicalViewer().setSelection(new StructuredSelection(newTopLevelParts));
	}

	private EditPart findEditPartForModel(EditPart root,Element model) {
		List<EditPart> children = root.getChildren();
		for (EditPart editPart : children) {
			if(editPart.getModel() == model)
				return editPart;
			else {
				EditPart findEditPartForModel = findEditPartForModel(editPart,model);
				if(findEditPartForModel != null)
					return findEditPartForModel;
			}
		}
		return null;
	}

	@Override
	public void redo() {
		Iterator<Element> it = list.values().iterator();
		Container container = (Container)pasteTarget.getModel();
		while (it.hasNext()) {
			container.addChild(it.next());
		}
	}

	@Override
	public boolean canUndo() {
		return !(list.isEmpty());
	}

	@Override
	public void undo() {
		Container container = (Container)pasteTarget.getModel();
		for (Iterator<Element> iter = list.values().iterator(); iter.hasNext();)
			container.removeChild(iter.next());
	}

	public boolean isPastableElement(Element element) {
		if(element.getPlatform().compareTo(((Element)pasteTarget.getModel()).getPlatform()) != 0)
			return false;
		if(element instanceof Picker && !((Element)pasteTarget.getModel()).getPlatform().isAndroid())
			return false;
		return GUIEditorPlugin.getComponentValidator().validate(element, pasteTarget.getModel());
	}
	
	protected Element cloneElement(Element oldPart, Container newParent) {
		Element newPart = null;
		
		if (oldPart instanceof TitaniumUIBaseElement){
			try {
				newPart = ((TitaniumUIBaseElement)oldPart).getCopy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalStateException("Cant clone this: " + oldPart);
		}
		
		newParent.addChild(newPart);
		
		if (oldPart instanceof Container) {
			for (Element child : ((Container) oldPart).getChildren()) {
				if(child != newPart && !GUIEditorPlugin.getComponentValidator().isAncestor(child,newPart))
					cloneElement(child, (Container) newPart);
			}
		}

		if (newPart instanceof TitaniumUIBoundedElement) {
			TitaniumUIBoundedElement titNewPart = (TitaniumUIBoundedElement) newPart;
			TitaniumUIBoundedElement titOldPart = (TitaniumUIBoundedElement) oldPart;
			titNewPart.setWidth(titOldPart.getWidth());
			titNewPart.setHeight(titOldPart.getHeight());
			titNewPart.setLeft(titOldPart.getLeft());
			titNewPart.setTop(titOldPart.getTop());
			titNewPart.setRight(titOldPart.getRight());
			titNewPart.setBottom(titOldPart.getBottom());
		}
		
		return newPart;
	}

}
