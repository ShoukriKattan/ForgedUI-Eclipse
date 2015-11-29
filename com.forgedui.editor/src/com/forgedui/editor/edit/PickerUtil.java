//LICENSE
package com.forgedui.editor.edit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.Picker.PickerType;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class PickerUtil {
		
	public static void changePickerType(PickerEditPart ep, PickerType old){
		try{
			switch (ep.getModel().getPickerType()) {
			case PICKER_TYPE_DATE:
				changeToDate(ep);
				break;
			case PICKER_TYPE_TIME:
				changeToTime(ep);
				break;
			case PICKER_TYPE_DATE_AND_TIME:
				changeToDateTime(ep);
				break;
			case PICKER_TYPE_COUNT_DOWN_TIMER:
				changeToCountDownTimer(ep);
				break;
			default:
				changeToPlain(ep, old);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private static void changeToDate(PickerEditPart ep) {
		Picker m = ep.getModel();
		Calendar c = Utils.getObject(m.getValue(), Calendar.getInstance());
		if (m.getPlatform().isAndroid() && Utils.getBoolean(m.getUseSpinner(), false)){
			makeStructure(m, 3, 4);//use one more row at the top to look better
			fillPlusMinus(m);
			fillDate(m, 2, c);
		} else {
			makeStructure(m, 3, 5);
			addDateTime(c, -2, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.YEAR);
			for (int i = 0; i < 5; i++) {
				fillDate(m, i, c);
				addDateTime(c, 1, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.YEAR);
			}//set time back!!!
			addDateTime(c, -3, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.YEAR);
		}
		
		col(m ,0).setWidth(ep.getSupport().viewToModel((int)(ep.getBounds().width / 2.5)));
	}
	
	private static void changeToTime(PickerEditPart ep) {
		Picker m = ep.getModel();
		Calendar c = Utils.getObject(m.getValue(), Calendar.getInstance());
		//default format24 value depends on useSpinner value
		//https://developer.appcelerator.com/apidoc/mobile/latest/Titanium.UI.Picker-object.html
		boolean useSpinner = m.getPickerType() == PickerType.PICKER_TYPE_TIME
			? Utils.getBoolean(m.getUseSpinner(), false) : false;//ignore for date_time picker
		boolean format24 = m.getPickerType() == PickerType.PICKER_TYPE_TIME
			? Utils.getBoolean(m.getFormat24(), useSpinner) : false;//ignore for date_time picker
		
		int hoursField = (format24 && m.getPickerType() == PickerType.PICKER_TYPE_TIME) ? Calendar.HOUR_OF_DAY : Calendar.HOUR;
		int columns = format24 ? 2 : 3;
		if (m.getPlatform().isAndroid() && useSpinner){
			makeStructure(m, columns, new int[]{4,4,3});//use one more row at the top to look better
			if (!format24){
				row(m, 2, 0).setTitle("");row(m, 2, 1).setTitle("");
				row(m, 2, 2).setTitle(c.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM");
			}
			fillPlusMinus(m);
			fillTime(m, 2, c, hoursField);
		} else {
			makeStructure(m, columns, new int[]{5,5,4});
			if (!format24){
				row(m, 2, 0).setTitle("");
				if (c.get(Calendar.HOUR_OF_DAY) >= 12){
					row(m, 2, 1).setTitle("AM");
					row(m, 2, 2).setTitle("PM");
					row(m, 2, 3).setTitle("");
				} else {
					row(m, 2, 1).setTitle("");
					row(m, 2, 2).setTitle("AM");
					row(m, 2, 3).setTitle("PM");
				}
			}
			
			addDateTime(c, -2, Calendar.HOUR_OF_DAY, Calendar.MINUTE);
			for (int i = 0; i < 5; i++) {
				fillTime(m, i, c, hoursField);
				addDateTime(c, 1, Calendar.HOUR_OF_DAY, Calendar.MINUTE);
			}
			//set time back!!!
			addDateTime(c, -3, Calendar.HOUR_OF_DAY, Calendar.MINUTE);
		}
	}
	
	private static void changeToDateTime(PickerEditPart ep) {
		changeToTime(ep);
		Picker m = ep.getModel();
		Calendar c = Utils.getObject(m.getValue(), Calendar.getInstance());
		
		PickerColumn col = new PickerColumn();
		m.addChild(col, 0);
		col.setRowCount(5);
		
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM d");
		c.add(Calendar.DAY_OF_MONTH, -2);
		for (int i = 0; i < 5; i++) {
			row(m, 0, i).setTitle(format.format(c.getTime()));
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		c.add(Calendar.DAY_OF_MONTH, -3);
		
		col(m ,0).setWidth(ep.getSupport().viewToModel((int)(ep.getBounds().width / 2.5)));
	}
	
	private static void changeToCountDownTimer(PickerEditPart ep) {
		Picker m = ep.getModel();
		Double seconds = Utils.getObject(m.getCountDownDuration(), new Double(1000*(3600 + 240)))/1000;
		int hours = (int)(seconds/3600);
		int minutes = (int)((seconds - hours*3600) / 60);
		
		makeStructure(m, 2, 5);
		
		String hourMinus1 = hours >= 1 ? hours - 1 + "" : "";
		String hourMinus2 = hours >= 2 ? hours - 2 + "" : "";
		String minMinus1 = minutes >= 1 ? minutes - 1 + "" : "";
		String minMinus2 = minutes >= 2 ? minutes - 2 + "" : "";
		
		row(m, 0, 0).setTitle(hourMinus2);
		row(m, 0, 1).setTitle(hourMinus1);
		row(m, 0, 2).setTitle(hours +" hours");
		row(m, 0, 3).setTitle(hours + 1 + "");
		row(m, 0, 4).setTitle(hours + 2 + "");
		
		row(m, 1, 0).setTitle(minMinus2);
		row(m, 1, 1).setTitle(minMinus1);
		row(m, 1, 2).setTitle(minutes + " minutes");
		row(m, 1, 3).setTitle((minutes + 1) % 60 + "");
		row(m, 1, 4).setTitle((minutes + 2) % 60 + "");
	}
	
	private static void changeToPlain(PickerEditPart ep, PickerType old) {
		Picker m = ep.getModel();
		makeStructure(m, 1, 1);
	}
	
	private static void fillDate(Picker m, int row, Calendar c){
		row(m, 0, row).setTitle(c.getDisplayName(
				Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
		row(m, 1, row).setTitle(c.get(Calendar.DAY_OF_MONTH) + "");
		row(m, 2, row).setTitle(c.get(Calendar.YEAR) + "");
	}
	
	private static void fillPlusMinus(Picker m){
		for (int i = 0; i < m.getChildren().size(); i++){
			row(m, i, 0).setTitle("");
			row(m, i, 1).setTitle("+");
			row(m, i, 3).setTitle("-");
		}
	}
	
	private static void fillTime(Picker m, int row, Calendar c, int hoursField){
		row(m, 0, row).setTitle("" + c.get(hoursField));
		row(m, 1, row).setTitle("" + c.get(Calendar.MINUTE));
	}
	
	private static void addDateTime(Calendar c, int amount, int... fields){
		for (int i = 0; i < fields.length; i++) {
			c.add(fields[i], amount);
		}
	}
	
	private static void clearPicker(Picker model){
		model.setColumnCount(0);
	}
	
	private static void makeStructure(Picker model, int col, int row){
		clearPicker(model);
		model.setColumnCount(col);
		for (int i = 0; i < model.getChildren().size(); i++) {
			col(model, i).setRowCount(row);
		}
	}
	
	private static void makeStructure(Picker model, int col, int[] rows){
		clearPicker(model);
		model.setColumnCount(col);
		for (int i = 0; i < col; i++) {
			col(model, i).setRowCount(rows[i]);
		}
	}
	
	private static PickerColumn col(Picker model, int num){
		return model.getChildren().get(num);
	}
	
	private static PickerRow row(Picker model, int col, int row){
		return col(model, col).getChildren().get(row);
	}

}
