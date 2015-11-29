//LICENSE
package com.forgedui.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DateTimeLableProvider extends LabelProvider {
	
	private boolean showTime;
	private boolean showDate;
	private boolean format24;

	public DateTimeLableProvider(boolean showTime, boolean showDate){
		this(showTime, showDate, false);
	}
	
	public DateTimeLableProvider(boolean showTime, boolean showDate, boolean format24){
		this.showTime = showTime;
		this.showDate = showDate;
		this.format24 = format24;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Calendar){
			Calendar c = (Calendar)element;
			Date d = c.getTime();
			if (showTime && showDate){
				return new SimpleDateFormat(getTimeFormat() + " MMMM dd yyyy").format(d);
			} else if (showTime){
				return new SimpleDateFormat(getTimeFormat()).format(d);
			} else if (showDate){
				return new SimpleDateFormat("MMMM dd yyyy").format(d);
			}
		} else if (element != null){
			System.out.println(element);
		}
		return null;
	}
	
	protected String getTimeFormat(){
		return format24 ? "HH:mm" : "hh:mm a";
	}
	
	public void setFormat24(boolean format24) {
		this.format24 = format24;
	}

	/**
	 * @param showTime the showTime to set
	 */
	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}

	/**
	 * @param showDate the showDate to set
	 */
	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

}
