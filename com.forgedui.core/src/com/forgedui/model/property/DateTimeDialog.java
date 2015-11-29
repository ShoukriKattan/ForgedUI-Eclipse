/**
 * 
 */
package com.forgedui.model.property;

import java.util.Calendar;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

import com.forgedui.util.Utils;


/**
 * @author Dmitry Grimm {dmitry.grimm@gmail.com}
 *
 */
public class DateTimeDialog extends SelectionDialog {
	
	private DateTime time, date;
	
	private Calendar currentValue, result;
	
	private String timme, datte;
	
	private boolean showTime, showDate;

	/**
	 * @param parentShell
	 * @param currentValue 
	 */
	public DateTimeDialog(Shell parentShell, Calendar currentValue, boolean showTime, boolean showDate) {
		super(parentShell);
		this.showTime = showTime;
		this.showDate = showDate;
		this.currentValue = currentValue;
		setTitle("Please select date and time");
		setHelpAvailable(false);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		// create a composite with standard margins and spacing
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(composite);
		
		if (showTime){
			Label timeLbl = new Label(composite, SWT.NULL);
			timeLbl.setText("Time:");
			time = new DateTime(composite, SWT.TIME);
			if (currentValue!= null){
				time.setTime(currentValue.get(Calendar.HOUR_OF_DAY),
						currentValue.get(Calendar.MINUTE),
						currentValue.get(Calendar.SECOND));
			}
		}
		
		if (showDate){
			Label dateLbl = new Label(composite, SWT.NULL);
			dateLbl.setText("Date:");
			date = new DateTime(composite, SWT.CALENDAR);
			if (currentValue!= null){
				date.setDate(currentValue.get(Calendar.YEAR),
					currentValue.get(Calendar.MONTH),
					currentValue.get(Calendar.DAY_OF_MONTH));
			}
		}
		
		return composite;
	}
	
	@Override
	protected void okPressed() {
		timme = getTime_();
		datte = getDate_();
		result = Calendar.getInstance();
		if (showDate && showTime){
			result.set(date.getYear(), date.getMonth(), date.getDay(),
					time.getHours(), time.getMinutes(), time.getSeconds());
		} else if (showDate){
			result.set(date.getYear(), date.getMonth(), date.getDay());
		} else if (showTime){
			result.set(time.getHours(), time.getMinutes(), time.getSeconds());
		}
				
		super.okPressed();
	}
	
	private String getTime_(){
		StringBuilder timme = new StringBuilder()
			.append(twoDijit(time.getHours()))
			.append(':').append(twoDijit(time.getMinutes()))
			.append(':').append(twoDijit(time.getSeconds()));
		return timme.toString();
	}
	
	private String getDate_(){
		StringBuilder datte = new StringBuilder()
			.append(date.getYear())
			.append('-').append(twoDijit(date.getMonth() + 1))
			.append('-').append(twoDijit(date.getDay()));
		return datte.toString();
	}
	
	protected String twoDijit(int number){
		return number <= 9 ? "0" + number : "" + number;
	}
	
	public String getTime(){
		return timme;
	}

	public String getDate(){
		return datte;
	}
	
	public Calendar getSelectedDateTime(){
		return result;
	}
	
	public void setDateTime(String dateTime){
		if (Utils.isNotEmpty(dateTime)){
			String[] parts = dateTime.split("T");
			if (parts.length == 2){
				int[] t = extractInts(parts[1].split(":"));
				if (t != null){
					time.setHours(t[0]);
					time.setMinutes(t[1]);
					time.setSeconds(t[2]);
				}
				int[] d = extractInts(parts[0].split("-"));
				if (d != null){
					date.setYear(d[0]);
					date.setMonth(d[1] - 1);
					date.setDay(d[2]);
				}
			}
		}
	}
	
	protected int[] extractInts(String[] parts){
		if (parts.length == 3){
			int[] res = new int[3];
			res[0] = getInt(parts[0]);
			res[1] = getInt(parts[1]);
			res[2] = getInt(parts[2]);
			if (res[0] >= 0 && res[1] >= 0 && res[2] >= 0){
				return res;
			}
		}
		return null;
	}
	
	protected int getInt(String s){
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
