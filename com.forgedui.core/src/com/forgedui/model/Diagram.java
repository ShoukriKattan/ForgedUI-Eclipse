// LICENSE
package com.forgedui.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.forgedui.core.ForgedUICorePlugin;
import com.forgedui.core.TitaniumProject;
import com.forgedui.model.property.ResolutionPropertyDescriptor;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.PlatformSupport;
import com.forgedui.model.titanium.Tab;
import com.forgedui.model.titanium.TabGroup;
import com.forgedui.model.titanium.annotations.Unmapped;



/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class Diagram extends ContainerImpl {
	
	public static String RESOLUTION = "Resolution";
	
	public static String PLATFORM = "Platform";
	
	private static final long serialVersionUID = 2L;
	
	private Platform platform;
	
	private Dimension resolution;
	
	@Unmapped
	private Dimension resForText;
	
	private transient PlatformSupport support;
	
	private Screen screen;
	
	private transient IFile file;
	
	/**
	 * The names used in the Diagram
	 */
	private Set<String> names = new HashSet<String>();
	
	private PropertyChangeListener elementsListener = new DiagramNamesCollector();
	
	public Diagram(Platform platform, Dimension resolution, Dimension realWindowResolution) {
		addPropertyChangeListener(elementsListener);
		screen = new Screen();
		screen.addPropertyChangeListener(elementsListener);
		screen.setParent(this);
		setPlatform(platform);
		setResolution(realWindowResolution);
		resForText = resolution;
	}
	
	public void setFile(IFile file) {
		this.file = file;		
	}

	@Override
	public void addChild(Element child, int index) {
		//Assert.isTrue(children.size() == 0, "Only 1 container is allowed!");
		super.addChild(child, index);
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		int size = 1;
		if(Platform.Android == platform)
			size = 2;
		IPropertyDescriptor[] descr = new IPropertyDescriptor[size];
		descr[0] = new PropertyDescriptor(Diagram.PLATFORM, Diagram.PLATFORM);
		if(Platform.Android == platform)
			descr[1] = new ResolutionPropertyDescriptor(getPlatform(), Diagram.RESOLUTION);
		return descr;
	}

	/**
	 * @return the platform
	 */
	public Platform getPlatform() {
		return this.platform;
	}
	
	public void setPlatform(Platform platform) {
		Platform old = this.platform;
		this.platform = platform;
		listeners.firePropertyChange(PLATFORM, old, platform);
	}
	
	/**
	 * @return the resolution
	 */
	public Dimension getResolution() {
		return this.resolution;
	}
	
	public void setResolution(Dimension resolution) {
		if (resolution == null){//null means default
			resolution = PlatformSupport.getBaseResolution(platform).getCopy();
		}
		if (this.support != null){
			support.setResolution(resolution);
		}
		Dimension old = this.resolution;
		this.resolution = resolution;
		listeners.firePropertyChange(RESOLUTION, old, resolution);
	}
	
	@Override
	public Diagram getDiagram() {
		return this;
	}
	
	public ICellEditorValidator getUniqueNameValidator(Element element){
		return new DiagramNamesValidator(element);
	}
	
	public String generateUniqueName(String baseName){
		int i = 1;
		while(names.contains(baseName + "_" + i)){
			i++;
		}
		return baseName + "_" + i;
	}
	
	public PropertyChangeListener getElementsListener(){
		return elementsListener;
	}
	
	class DiagramNamesCollector implements PropertyChangeListener, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getOldValue() instanceof Element){
				unregisterName((Element) evt.getOldValue());
				((Element) evt.getOldValue()).removePropertyChangeListener(this);
			}
			if (evt.getNewValue() instanceof Element){
				((Element) evt.getNewValue()).addPropertyChangeListener(this);
				registerName((Element) evt.getNewValue());
			}
			if (Element.PROPERTY_NAME.equals(evt.getPropertyName())){
				if (evt.getOldValue() != null){
					unregisterName(evt.getOldValue().toString());
				}
				if (evt.getNewValue() != null){
					registerName((Element) evt.getSource());
				}
			}
		}
		
		public void registerName(Element e){
			names.add(e.getName());
			if (e instanceof Container){//FIXME use model children here
				List<? extends Element> children = ((Container)e).getChildren();
				for (Element child : children) {
					child.addPropertyChangeListener(elementsListener);
					registerName(child);
				}
			}
		}
		
		public void unregisterName(Element e){
			unregisterName(e.getName());
			if (e instanceof Container){//FIXME use model children here
				List<? extends Element> children = ((Container)e).getChildren();
				for (Element child : children) {
					child.removePropertyChangeListener(elementsListener);
					unregisterName(child);
				}
			}
		}
		
		public void unregisterName(String name){
			names.remove(name);
		}
	}

	class DiagramNamesValidator implements ICellEditorValidator, Serializable {
		private static final long serialVersionUID = 1L;
		
		private Element element;
		
		public DiagramNamesValidator(Element element){
			this.element = element;
		}

		@Override
		public String isValid(Object value) {
			if (value == null || value.toString().trim().length() == 0)
				return "Name could not be empty";
			if (value.toString().equals(element.getName())) 
				return null;
			if (!checkJSConvention(value.toString())){
				return "Invalid JS identifier";
			}
			return !names.contains(value.toString()) ? null
					: "The name is not unique";
		}
		
		public boolean checkJSConvention(String name){
			return ForgedUICorePlugin.JSS_NAME_PATTERN.matcher(name).matches();
		}

	};
	
	public Screen getScreen(){
		return screen;
	}

	public IFile getFile() {
		return file;
	}
	
	public TitaniumProject getProject(){
		return (TitaniumProject) getFile().getProject().getAdapter(TitaniumProject.class);
	}
	
	public TabGroup getTabGroupDiagram(){
		TitaniumProject proj = getProject();
		if (proj != null){
			Collection<Diagram> tgDiagrams = proj.getTabGroupDiagrams();
			if (!tgDiagrams.isEmpty()){
				if (getScreen().getWindow() != null){
					//Find TabGroup associated with current window
					String windowName = getScreen().getWindow().getName();
					Diagram[] tabGroups = tgDiagrams.toArray(new Diagram[tgDiagrams.size()]);
					for (Diagram tgDiagram : tabGroups) {
						TabGroup tg = tgDiagram.getScreen().getTabGroup();
						List<Tab> tabs = tg.getTabs();
						for (int i = 0; i < tabs.size(); i++) {
							if (windowName.equals(tabs.get(i).getWindow())){
								return tg;
							}
						}
					}
				}
				//return the first one
				//Diagram tgDiagram = tgDiagrams.iterator().next();
				//return (TabGroup)tgDiagram.getScreen().getTabGroup();
			}
		}
		return null;
	}
	
	public Collection<Diagram> getProjectDiagrams(){
		TitaniumProject proj = getProject();
		if (proj != null){
			return proj.getDiagrams();
		}
		return null;
	}
	
	public PlatformSupport getSupport(){
		if (this.support == null){
			this.support = new PlatformSupport(getPlatform(), getResolution());
		}
		return this.support;
	}
	
	@Override
	public Object getPropertyValue(Object propertyId) {
		if (PLATFORM.equals(propertyId)){
			return getDiagram().getPlatform();
		} else if (RESOLUTION.equals(propertyId)){
			return getResolution();
		}
		return super.getPropertyValue(propertyId);
	}
	
	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (Diagram.RESOLUTION.equals(propertyId)){
			setResolution((Dimension) value);
		} else
			super.setPropertyValue(propertyId, value);
	}

	public Dimension getResForText() {
		return resForText;
	}
	
}

