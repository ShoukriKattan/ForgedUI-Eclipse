// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.forgedui.model.titanium.MoreTab;
import com.forgedui.model.titanium.Tab;
import com.forgedui.model.titanium.TabGroup;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ExternalTabGroup extends TabGroup implements PropertyChangeListener {
	
	private static final long serialVersionUID = 1L;

	private TabGroup original;
	
	private Map<Tab, ExternalTab> map = null;

	ExternalTabGroup(TabGroup tg){
		this.original = tg;
		original.addPropertyChangeListener(this);
	}
	
	private Map<Tab, ExternalTab> getTabsMap(){
		//need this to initialize tabs
		if (map == null){
			map = new HashMap<Tab, ExternalTab>();
			for (Tab tab : original.getTabs()) {
				ExternalTab et =
					(tab instanceof MoreTab)
						? new ExternalMoreTab(tab)
						: new ExternalTab(tab);
				et.setParent(this);
				map.put(tab, et);
			}
		}
		return map;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		//TODO make existing prop descriptors read only
		return new IPropertyDescriptor[0];
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("TabGroup.tab".equals(evt.getPropertyName())){
			if (evt.getNewValue() == null){
				ExternalTab et = getTabsMap().remove(evt.getOldValue());
				et.setParent(null);
			} else if (evt.getOldValue() == null){
				ExternalTab et = new ExternalTab((Tab)evt.getNewValue());
				et.setParent(this);
				getTabsMap().put((Tab)evt.getNewValue(), et);
			}
		}
		this.listeners.firePropertyChange(evt);
	}
	
	@Override
	public String getName() {
		return original.getName();
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
	public List<Tab> getTabs() {
		List<Tab> list = new ArrayList<Tab>();
		for (Tab tab : original.getTabs()) {//to save order
			list.add(getTabsMap().get(tab));
		}
		return Collections.unmodifiableList(list);
	}
	
	@Override
	public int getTabNumber() {
		return getTabsMap().size();
	}
	
	/*@Override
	public Dimension getDimension() {
		return original.getDimension();
	}
	
	@Override
	public Point getLocation() {
		return original.getLocation();
	}*/
	
	public TabGroup getOriginal(){
		return original;
	}
	
}