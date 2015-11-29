package com.forgedui.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdapterFactory;

@SuppressWarnings("rawtypes")
public class TitaniumProjecAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof IProject
				&& adapterType == TitaniumProject.class){
			return ForgedUICorePlugin.getTitaniumProject((IProject) adaptableObject);
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[]{TitaniumProject.class};
	}

}
