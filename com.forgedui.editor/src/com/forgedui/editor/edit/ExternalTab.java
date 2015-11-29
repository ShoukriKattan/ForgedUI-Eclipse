// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.forgedui.model.titanium.Tab;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ExternalTab extends Tab implements PropertyChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	private Tab original;
	
	ExternalTab(Tab tab){
		this.original = tab;
		original.addPropertyChangeListener(this);
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		//TODO make existing prop descriptors read only
		return new IPropertyDescriptor[0];
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		//TODO do we need this???
	}
	
	@Override
	public String getName() {
		return original.getName();
	}
	
	@Override
	public String getTitle() {
		return original.getTitle();
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		original.addPropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		original.removePropertyChangeListener(listener);
	}
	
	@Override
	public String getIcon() {
		return original.getIcon();
	}
	
	@Override
	public String getBadge() {
		return original.getBadge();
	}
	
	@Override
	public String getWindow() {
		return original.getWindow();
	}

	/*@Override
	public Dimension getDimension() {
		return original.getDimension();
	}
	
	@Override
	public Point getLocation() {
		return original.getLocation();
	}*/
	
	public Tab getOriginal(){
		return original;
	}
	
}
