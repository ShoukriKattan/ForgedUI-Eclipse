// LICENSE
package com.forgedui.model.property;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.PlatformSupport;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ResolutionCellEditor extends DialogCellEditor {
	
	private LabelProvider labelProvider;
	private Platform platform;
	
	public ResolutionCellEditor(Composite parent, Platform platform){
		super(parent);
		this.platform = platform;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		List<Dimension> dimensions = PlatformSupport.getResolutions(platform);
		List<Dimension> res = new LinkedList<Dimension>();
		for (int i = 0; i < dimensions.size(); i++) {
			if (dimensions.get(i).width <= dimensions.get(i).height){
				res.add(dimensions.get(i));
			}
		}
		DimensionDialog dialog = new DimensionDialog(null, (Dimension) getValue(),
				res);
		if (dialog.open() == Window.OK){
			return dialog.getResolution();
		}
		return null;
	}
	
	@Override
	public void create(Composite parent) {
		labelProvider = new ResolutionLabelProvider();
		super.create(parent);
	}
	
	protected void updateContents(Object value) {
		super.updateContents(labelProvider.getText(value));
	}
	
	class DimensionDialog extends Dialog {
		
		private LabelProvider labelProvider = new ResolutionLabelProvider();
		private Dimension resolution;
		private List<Dimension> defaultResolutions;
		private Combo resolutionField;
		private Button isLandscape;
		private Spinner customWidth;
		private Spinner customHeight;
		private Button customResolution;

		/**
		 * @param parentShell
		 */
		protected DimensionDialog(Shell parentShell, Dimension resolution,
				List<Dimension> defaultResolutions) {
			super(parentShell);
			this.resolution = resolution;
			this.defaultResolutions = defaultResolutions;
		}
		
		@Override
		protected Control createDialogArea(Composite parent) {
			getShell().setText("Change resolution");
			Composite top = (Composite) super.createDialogArea(parent);
			((GridLayout)top.getLayout()).numColumns = 2;

			SelectionListener resolutionListener = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					updateResolution();
				}
			};
			
			Label label = new Label(top, SWT.NONE);
			label.setText("Default");
			resolutionField = new Combo(top, SWT.BORDER | SWT.READ_ONLY);
			resolutionField.addSelectionListener(resolutionListener);
			isLandscape = new Button(top, SWT.CHECK);
			isLandscape.setText("Landscape");
			isLandscape.addSelectionListener(resolutionListener);
			
			customResolution = new Button(top, SWT.CHECK);
			customResolution.setText("Custom");
			customResolution.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					resolutionField.setEnabled(!customResolution.getSelection());
					isLandscape.setEnabled(!customResolution.getSelection());
					customWidth.setEnabled(customResolution.getSelection());
					customHeight.setEnabled(customResolution.getSelection());
					if (customResolution.getSelection()){
						customWidth.setSelection(resolution.width);
						customHeight.setSelection(resolution.height);
					} else {
						updateResolution();
					}
				}
			});
			
			GridData gd = new GridData();
			gd.horizontalSpan = 2;
			customResolution.setLayoutData(gd);

			label = new Label(top, SWT.NULL);
			label.setText("Width");
			
			
			GridData gd2 = new GridData(80, -1);
			customWidth = new Spinner(top, SWT.NONE);
			customWidth.setValues(320, 100, 2000, 0, 10, 100);
			customWidth.setEnabled(false);
			customWidth.setLayoutData(gd2);
			customWidth.addSelectionListener(resolutionListener);
			
			label = new Label(top, SWT.NULL);
			label.setText("Height");
			
			customHeight = new Spinner(top, SWT.NONE);
			customHeight.setValues(480, 100, 2000, 0, 10, 100);
			customHeight.setEnabled(false);
			customHeight.setLayoutData(gd2);
			customHeight.addSelectionListener(resolutionListener);
			
			initResolutions();
			if(ResolutionCellEditor.this.platform != Platform.Android)
				customResolution.setEnabled(false);
			return parent;
		}

		/**
		 * 
		 */
		private void initResolutions() {
			int currentResolution = -1;
			String[] res = new String[defaultResolutions.size()];
			for (int i = 0; i < defaultResolutions.size(); i++) {
				res[i] = labelProvider.getText(defaultResolutions.get(i));
				if (resolution.equals(defaultResolutions.get(i))){
					currentResolution = i;
					isLandscape.setSelection(false);
				} else if (resolution.getTransposed().equals(defaultResolutions.get(i))){
					currentResolution = i;
					isLandscape.setSelection(true);
				}
			}
			resolutionField.setItems(res);
			resolutionField.setData(defaultResolutions);

			if (currentResolution >= 0){
				resolutionField.select(currentResolution);
			} else {
				customWidth.setSelection(resolution.width);
				customHeight.setSelection(resolution.height);
				resolutionField.select(0);
				resolutionField.setEnabled(false);
				customHeight.setEnabled(true);
				customWidth.setEnabled(true);
				customResolution.setSelection(true);
			}
		}

		/**
		 * 
		 */
		@SuppressWarnings("unchecked")
		protected void updateResolution() {
			if (resolutionField.isEnabled()){
				List<Dimension> dimensions = (List<Dimension>) resolutionField
						.getData();
				Dimension dimension = dimensions.get(resolutionField.getSelectionIndex());
				resolution = isLandscape.getSelection() ? dimension.getTransposed() : dimension;
			} else {
				resolution = new Dimension(customWidth.getSelection(),
						customHeight.getSelection());
			}
		}
		
		public Dimension getResolution(){
			if(Platform.Android == platform)
				return resolution;
			else{
				List<Dimension> resolutions = PlatformSupport.getResolutions(platform);
				List<Dimension> window_resolutions = PlatformSupport.getWindowResolutions(platform);
				for (int i = 0; i < resolutions.size() ; i++) {
					if(resolution.equals(resolutions.get(i)))
						return window_resolutions.get(i);
				}
				return null;
			}
		}
		
	}
}