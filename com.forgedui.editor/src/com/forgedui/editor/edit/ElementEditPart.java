// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.rulers.RulerProvider;

import com.forgedui.editor.defaults.DefaultsProvider;
import com.forgedui.editor.defaults.IDefaultsProvider;
import com.forgedui.editor.edit.policy.ContainerHighlightEditPolicy;
import com.forgedui.editor.edit.policy.DeleteElementEditPolicy;
import com.forgedui.editor.edit.policy.ElementDirectEditPolicy;
import com.forgedui.editor.edit.policy.xpl.SnapFeedbackPolicy;
import com.forgedui.editor.figures.FiguresFactory;
import com.forgedui.editor.figures.TitaniumFigure;
import com.forgedui.editor.util.BoundsHelper;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.PlatformSupport;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.util.ScreenManager;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public abstract class ElementEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {
	
	protected static Comparator<Object> zIndexComparator = new ZIndexComparator();
	
	@Override
	protected IFigure createFigure() {
		return FiguresFactory.createFigure(getElement());
	}
	
	@Override
	public Element getModel() {
		return (Element) super.getModel();
	}

	protected void createEditPolicies() {
		installEditPolicy(DeleteElementEditPolicy.KEY, new DeleteElementEditPolicy());
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
		// Adding the direct editing for the elements at the edit parts.
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new ElementDirectEditPolicy());
	}
	
	/**
	 * This method sort children list according to their zIndexes.
	 * Children should override {@link #getModelChildren_() getModelChildren_()} instead
	 */
	protected List<?> getModelChildren(){
		//need to create new collection as returned collection could be readonly
		 List children = new ArrayList<Object>(getModelChildren_());
		 List expandedPickers = null;
		 if (getModel() instanceof Container) {
			 expandedPickers = getExpandedPickers((Container)getModel());
			 children.removeAll(expandedPickers);
		 }
		 Collections.sort(children, zIndexComparator);
		 
		 if(expandedPickers != null)
			 for (int i= 0; i < expandedPickers.size(); i++) {
				 children.add(expandedPickers.get(i));
			 }
		 return children;
	}
	
	protected List<Element> getExpandedPickers(Container parent){
		List<Element> pickers = new ArrayList<Element>();
		if (parent != null && getModel().getPlatform().isAndroid()){
			List<? extends Element> children = parent.getChildren();
			for (Element element : children) {
				if (element instanceof Picker) {
					Picker picker = (Picker) element;
					if (picker.isExpanded()) pickers.add(picker);
				} else if (element instanceof Container){
					pickers.addAll(getExpandedPickers((Container) element));
				}
			}
		}
		return pickers;
	}
	
	/**
	 * Implement 
	 * @return
	 */
	protected List<?> getModelChildren_(){
		return super.getModelChildren();
	}
	
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			getElement().addPropertyChangeListener(this);
		}
	}

	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			getElement().removePropertyChangeListener(this);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (Element.PROPERTY_SIZE.equals(prop) || Element.PROPERTY_LOCATION.equals(prop)) {
			refresh();
		} else if (TitaniumUIElement.PROPERTY_ZINDEX.equals(prop)){
			if (getParent() != null){
				getParent().refresh();
			}
		}
	}

	protected void refreshVisuals() {
		// notify parent container of changed position & location
		// if this line is removed, the XYLayoutManager used by the parent container 
		// (the Figure of the ElementsDiagramEditPart), will not know the bounds of this figure
		// and will not draw it correctly.
		//((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(),  getBounds());
		getFigure().setBounds(getBounds());
	}
	
	/**
	 * 
	 * @return model bounds
	 */
	public Rectangle getBounds(){
		return getSupport().modelToView(BoundsHelper.getBounds(this));
	}
	
	public void setVisualLocation(Point location){
		setModelLocation(getSupport().viewToModel(location));
	}
	
	public void setModelLocation(Point location){
		BoundsHelper.setLocation(this, location);
	}
	
	public void setVisualDimension(Dimension dimension){
		setModelDimension(getSupport().viewToModel(dimension));
	}
	
	public void setModelDimension(Dimension dimension){
		BoundsHelper.setDimension(this, dimension);
	}
	
	public Dimension getModelClientAreaSize(){
		Dimension visualDimension = getFigure().getClientArea().getSize();
		return getSupport().viewToModel(visualDimension);
	}
	
	/**
	 * This will return the model screen bounds.
	 * @return
	 */
	public Dimension getScreenBounds() { 
		Element model = getElement();
		return ScreenManager.getScreenWindowBounds(model);
	}
	
	public Object getAdapter(Class adapter) {
		if (adapter == SnapToHelper.class) {
			List<SnapToHelper> snapStrategies = new ArrayList<SnapToHelper>();
			Boolean val = (Boolean)getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
			/*if (val != null && val.booleanValue())*/
				//snapStrategies.add(new SnapToGuides((GraphicalEditPart) this.getParent()));

			val = (Boolean)getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
			//if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGeometry(this){
					@Override
					protected List generateSnapPartsList(List exclusions) {
						List children = super.generateSnapPartsList(exclusions);
						children.add(container);//add snapToParent edges behaviour
						return children;
					}
				});

			val = (Boolean)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGrid(this));
			
			if (snapStrategies.size() == 0)
				return null;
			if (snapStrategies.size() == 1)
				return snapStrategies.get(0);

			SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
			for (int i = 0; i < snapStrategies.size(); i++)
				ss[i] = (SnapToHelper)snapStrategies.get(i);
			return new CompoundSnapToHelper(ss);
		}
		return super.getAdapter(adapter);
	}
	
	public PlatformSupport getSupport(){
		return getElement().getSupport();
	}
	
	public IDefaultsProvider getDefaults(){
		return DefaultsProvider.getDefaultsProvider(getElement().getPlatform());
	}

	public Element getElement() {
		return (Element)getModel();
	}
	
	public void performRequest(Request request) {
		if (directEditingEnabled()) {
			if (request.getType() == RequestConstants.REQ_OPEN) { 
				// Here we can deal with things as a double click if we really want
				// that. But for now lets do it the standard way gef does.
				performDirectEdit();
			}
			if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
				//performDirectEdit();
			}
		}
	}
	
	public ScreenEditPart getScreenEditPart(){
		EditPart parent = this;
		while (parent != null && !(parent instanceof ScreenEditPart)) {
			parent = parent.getParent();
		}
		return (ScreenEditPart) parent;
	}
	
	/**
	 * By default we don't want to install 
	 * the direct editing as a lots of components
	 * just doesn't support it, we will have to override
	 * this method to make the editing valid.
	 * @return
	 */
	protected boolean directEditingEnabled() { 
		return false;
	}
	
	/**
	 * Just in case you don't want to uninstall the direct editing
	 * override this method to do nothing.
	 */
	private void performDirectEdit() {
		new ElementEditManager(this, new ElementCellEditorLocator(getFigure())).show();
	}
	
	@Override
	public void removeNotify() {
		dispose();
		super.removeNotify();
	}

	/**
	 * Imitation of dispose method.
	 * Allows to cleanup the resources like fonts, images, color, etc.
	 */
	public void dispose() {
		if (getFigure() instanceof TitaniumFigure){
			((TitaniumFigure) getFigure()).dispose();
		}
	}

	
}
