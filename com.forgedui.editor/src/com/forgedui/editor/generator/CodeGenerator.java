package com.forgedui.editor.generator;

import java.util.List;

import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.TitaniumUIBaseElement;

public interface CodeGenerator {

	public abstract void generateCodeForModel(
			TitaniumUIBaseElement rootElement, Platform platform)
			throws Exception;

	public abstract List<String> getBuildLog();

	public abstract List<String> getGeneratedCodeLines();

	public abstract List<String> getGeneratedJSSLines();

	

}