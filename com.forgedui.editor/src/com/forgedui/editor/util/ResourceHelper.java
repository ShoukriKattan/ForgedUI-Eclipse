// LICENSE
package com.forgedui.editor.util;

import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ResourceHelper {
	
	public static String getString(String text, String textid){
		//FIXME Read text by ID from resource file
		if (textid != null){
			textid = "#" + textid;
		}
		return Utils.getString(text, textid);
	}

}
