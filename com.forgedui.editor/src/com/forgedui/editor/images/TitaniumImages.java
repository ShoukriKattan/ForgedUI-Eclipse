// LICENSE
package com.forgedui.editor.images;

import java.text.MessageFormat;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.model.titanium.Platform;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitaniumImages {
	
	private final static String FIGURE_IMAGE = "icons/figures/{0}/{1}";//platform, name
	
	public static String CANCEL = "cancel.png";
	
	public static String HAS_CHILD = "has_child.png";
	
	public static String HAS_CHECK = "has_check.png";
	
	public static String CHECK_FALSE = "check_false.png";
	
	public static String CHECK_TRUE = "check_true.png";
	
	public static String HAS_DETAIL = "has_detail.png";
	
	public static String SIGNAL = "signal.png";
	
	public static String SIGNAL_3G = "signal_3g.png";
	
	public static String BATTERY = "battery.png";
	
	public static String ZOOM = "zoom.png";
	
	private static ImageRegistry registry = new ImageRegistry(Display.getCurrent());
	
	public static Image getImage(Platform p, String name){
		String key = MessageFormat.format(FIGURE_IMAGE,
				p.toString().toLowerCase(), name);
		if (registry.get(key) == null){
			registry.put(key, GUIEditorPlugin.getImageDescriptor(key));
		}
		return registry.get(key);
	}

}
