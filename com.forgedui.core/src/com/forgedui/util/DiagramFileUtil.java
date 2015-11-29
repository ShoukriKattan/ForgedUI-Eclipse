// LICENSE
package com.forgedui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.forgedui.core.ForgedUICorePlugin;
import com.forgedui.model.Diagram;
import com.forgedui.model.ForgeduiModel;
import com.forgedui.model.property.ColorCellEditor2;
import com.forgedui.model.property.FontCustomDialog;
import com.forgedui.model.titanium.ProgressBar;
import com.forgedui.model.titanium.Slider;
import com.forgedui.swt.dialogs.ImageTree;
import com.thoughtworks.xstream.XStream;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class DiagramFileUtil {

	private static XStream xStream;

	public static InputStream getDiagramContents(Diagram diagram) {
		ByteArrayOutputStream baos = null;
		try {
			ForgeduiModel model = new ForgeduiModel();
			model.setDiagram(diagram);
			model.setVersion(ForgedUICorePlugin.CURRENT_VERSION);

			baos = new ByteArrayOutputStream();
			XStream xStream = getXStreamInstance();
			xStream.toXML(model, baos);
			baos.flush();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		return bais;
	}

	public static Diagram getDiagram(IFile file) throws IOException, CoreException, ClassNotFoundException {
		XStream xStream = getXStreamInstance();
		InputStream in = file.getContents();
		ForgeduiModel model = (ForgeduiModel) xStream.fromXML(in);
		Diagram diagram = model.getDiagram();
		diagram.setFile(file);
		in.close();
		return diagram;
	}

	public static Diagram getDiagramOrNull(IFile file) {
		Diagram diagram = null;
		try {
			diagram = getDiagram(file);
		} catch (Exception e) {
			ForgedUICorePlugin.logError(e.getLocalizedMessage(), e);
		}
		return diagram;
	}

	private static XStream getXStreamInstance() {
		if (xStream == null) {

			xStream = new XStream();

			// TODO: excluding below properties prevents objects to be
			// initialized correctly
			// xStream.omitField(ElementImpl.class, "listeners");
			// xStream.omitField(Diagram.class, "elementsListener");

			// Omit unnecessary properties
			xStream.omitField(Diagram.class, "support");
			xStream.omitField(Diagram.class, "file");
			xStream.omitField(ColorCellEditor2.class, "buttonFocusListener");
			xStream.omitField(FontCustomDialog.class, "modifyListener");
			xStream.omitField(FontCustomDialog.class, "selectionListener");
			xStream.omitField(ProgressBar.class, "minValidator");
			xStream.omitField(ProgressBar.class, "maxValidator");
			xStream.omitField(ProgressBar.class, "valueValidator");
			xStream.omitField(Slider.class, "minValidator");
			xStream.omitField(Slider.class, "maxValidator");
			xStream.omitField(Slider.class, "valueValidator");
			xStream.omitField(ImageTree.class, "doubleClickListener");

			// Set class loader
			xStream.setClassLoader(ForgeduiModel.class.getClassLoader());

		}
		return xStream;
	}

}
