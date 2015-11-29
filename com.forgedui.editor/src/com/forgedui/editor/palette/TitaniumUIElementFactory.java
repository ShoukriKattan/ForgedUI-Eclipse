// LICENSE
package com.forgedui.editor.palette;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.requests.SimpleFactory;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.analytics.ReportingEventTypes;
import com.forgedui.editor.defaults.DefaultsProvider;
import com.forgedui.model.Diagram;
import com.forgedui.model.Element;
import com.forgedui.model.ElementImpl;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class TitaniumUIElementFactory extends SimpleFactory {

	private Diagram d;

	/**
	 * @param aClass
	 */
	public TitaniumUIElementFactory(Class<? extends Element> aClass, Diagram d) {
		super(aClass);
		this.d = d;
	}

	public Object getNewObject() {
		Object o = super.getNewObject();
		Element e = (Element) o;
		Dimension d = this.d.getSupport().viewToModel(
				DefaultsProvider.getDefaultsProvider(this.d.getPlatform())
						.getSize(e, this.d.getSupport()));
		if (e instanceof Picker && this.d.getPlatform().isAndroid()){
			d = this.d.getSupport().viewToModel(new Dimension(200, 40));
			((Picker)e).setExpanded(false);		
		}
		if (e instanceof TitaniumUIBoundedElement) {
			TitaniumUIBoundedElement titaniumElement = (TitaniumUIBoundedElement) e;
			titaniumElement.setWidth(""+d.width);
			titaniumElement.setHeight(""+d.height);
		} else {
			ElementImpl ei = (ElementImpl) e;
			ei.setDimension(d);
		}
		
		GUIEditorPlugin
		.getDefault()
		.getReportingService()
		.reportEvent(
				ReportingEventTypes.COMPONENT_CREATED,
				new String[] {
						o.getClass().toString(),
						this.d.getPlatform().toString() });

		return o;
	}

}
