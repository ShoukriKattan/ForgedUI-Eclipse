// LICENSE
package com.forgedui.editor.defaults;

import com.forgedui.model.titanium.Platform;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DefaultsProvider {
		
	private static IDefaultsProvider iPhoneProvider;
	private static IDefaultsProvider iPadProvider;
	private static IDefaultsProvider androidProvider;
	
	private DefaultsProvider(){
		
	}
	
	public static IDefaultsProvider getDefaultsProvider(Platform p){
		switch (p) {
		case iPhone:
			return getIPhoneProvider();
		case iPad:
			return getIPadProvider();
		case Android:
			return getAndroidProvider();
		}
		return null;
	}
	
	private static IDefaultsProvider getIPhoneProvider(){
		if (iPhoneProvider == null){
			iPhoneProvider = new IphoneDefaultsProvider(); 
		}
		return iPhoneProvider;
	}
	
	private static IDefaultsProvider getIPadProvider(){
		if (iPadProvider == null){
			iPadProvider = new IpadDefaultProvider(); 
		}
		return iPadProvider;
	}
	
	private static IDefaultsProvider getAndroidProvider(){
		if (androidProvider == null){
			androidProvider = new AndroidDefaultsProvider(); 
		}
		return androidProvider;
	}

}
