package com.forgedui.editor.jobs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.analytics.ReportingService;
import com.forgedui.editor.generator.AbstractTitaniumCodeGenerator;
import com.forgedui.editor.generator.BasicCodeGenerator;
import com.forgedui.editor.generator.CodeGenerator;
import com.forgedui.editor.generator.CommonJSModuleCodeGenerator;
import com.forgedui.editor.generator.Logger;
import com.forgedui.editor.generator.ObjectOrientedModuleCodeGenerator;
import com.forgedui.editor.preference.CodeGenerationPreferences;
import com.forgedui.model.Diagram;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.TitaniumUIBaseElement;
import com.forgedui.util.DiagramFileUtil;

/**
 * The code gen job.
 * 
 * @author Tareq Doufish
 * 
 */
public class CodeGenJob implements IEditorJob {

	private IFile diagramFile;

	public CodeGenJob(IFile diagramFile) {
		this.diagramFile = diagramFile;
	}

	public String getJobDescription() {
		return getJobName();
	}

	public String getJobName() {
		return "Editor code generator";
	}

	public int getTotalTime() {
		return 100;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {

		try {

			if (this.diagramFile == null)
				return;

			monitor.subTask("Generating file.");
			if (monitor.isCanceled()) {
				return;
			}
			monitor.worked(1);

			Diagram d = getDiagram(diagramFile);

			if (d != null) {

				IProject project = diagramFile.getProject();

				IFolder buildFolder = project.getFolder("build");

				monitor.subTask("Building files.");
				if (monitor.isCanceled()) {
					return;
				}
				monitor.worked(1);

				// Yes force creation, and make it local and i dont need
				// progress
				try {
					if (!buildFolder.exists()) {
						buildFolder.create(true, true, null);
					}
				} catch (CoreException e) {
					e.printStackTrace();
					return;
				}

				monitor.subTask("Building code.");
				if (monitor.isCanceled()) {
					return;
				}
				monitor.worked(1);

				GUIEditorPlugin plugin = GUIEditorPlugin.getDefault();
				final ILog log = plugin.getLog();

				IPreferenceStore preferenceStore = plugin.getPreferenceStore();

				Logger logger = new Logger() {

					@Override
					public void logError(String msg, Throwable e) {

						log.log(new Status(IStatus.ERROR,
								GUIEditorPlugin.PLUGIN_ID,
								"Error while generating code", e));

					}
				};

				ReportingService reportingService = plugin
						.getReportingService();

				boolean toSupportJSS = preferenceStore
						.getBoolean(CodeGenerationPreferences.CODE_GENERATION_STYLE_SUPPORT_JSS_PROPERTY_NAME);

				int codegenStyle = preferenceStore
						.getInt(CodeGenerationPreferences.CODE_GENERATION_STYLE_PROPERTY_NAME);

				CodeGenerator tiCodeGen = null;

				switch (codegenStyle) {

				case CodeGenerationPreferences.CODE_GEN_STYLE_VALUE_OO_MODULE:
					tiCodeGen = new ObjectOrientedModuleCodeGenerator(logger,
							reportingService, toSupportJSS);

					break;
				case CodeGenerationPreferences.CODE_GEN_STYLE_VALUE_COMMON_JS:
					tiCodeGen = new CommonJSModuleCodeGenerator(logger,
							reportingService, toSupportJSS);
					break;

				case CodeGenerationPreferences.CODE_GEN_STYLE_VALUE_BASIC:
				default:
					tiCodeGen = new BasicCodeGenerator(logger,
							reportingService, toSupportJSS);
					break;
				}

				// The diagram always has a screen. And this screen always has
				// something as base. either a window or something else:

				TitaniumUIBaseElement rootElement = null;

				Screen screen = d.getScreen();

				// What are we generating code for ?

				if (screen.getWindow() != null) {
					rootElement = screen.getWindow();
				} else {
					if (screen.getTabGroup() != null) {
						rootElement = screen.getTabGroup();
					}else{
						
						if(screen.getDialog()!=null){
							// We are sure ..
							rootElement   = (TitaniumUIBaseElement) screen.getDialog();
						}
						
					}
				}

				
				
				
				if (rootElement == null) {

					// then get the code lines and work happily ever after
					monitor.setCanceled(true);
					return;

				}

				tiCodeGen.generateCodeForModel(rootElement, d.getPlatform());
				List<String> code = tiCodeGen.getGeneratedCodeLines();
				List<String> buildLog = tiCodeGen.getBuildLog();
				List<String> jssLog = tiCodeGen.getGeneratedJSSLines();

				String fileName = diagramFile.getName();
				String fileExten = diagramFile.getFileExtension();

				String noExtensionName = fileName.substring(0,
						fileName.lastIndexOf("." + fileExten));

				String codeFileName = noExtensionName + ".js";
				String jssFileName = noExtensionName + ".jss";
				String logFileName = noExtensionName + "_build.log";

				IFile codeFile = buildFolder.getFile(codeFileName);
				IFile logFile = buildFolder.getFile(logFileName);

				InputStream codeIs = getInputStreamFromLines(code);
				InputStream logIs = getInputStreamFromLines(buildLog);

				writeToFile(codeFile, codeIs);

				if (toSupportJSS) {
					writeToFile(buildFolder.getFile(jssFileName),
							getInputStreamFromLines(jssLog));
				}

				writeToFile(logFile, logIs);

				// Log file we dont care

			} else {
				// TODO: throw error which should be displayed ?
				// Is it possible that file is corrupted

			}

			monitor.subTask("Finishing.");
			if (monitor.isCanceled()) {
				return;
			}
			monitor.worked(1);

		} catch (Exception e) {

			GUIEditorPlugin.logError("Error while generating code", e);

			if (e instanceof RuntimeException) {

				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}

		}

	}

	private void writeToFile(IFile file, InputStream is) {

		try {

			if (file.exists()) {
				file.setContents(is, true, true, null);
			} else {
				file.create(is, true, null);
			}

		} catch (Exception e) {

		}

	}

	private InputStream getInputStreamFromLines(List<String> code) {

		StringBuffer sb = new StringBuffer();

		for (String s : code) {
			sb.append(s);
			sb.append(AbstractTitaniumCodeGenerator.LINE_SEP);
			sb.append(AbstractTitaniumCodeGenerator.LINE_SEP);
		}

		try {
			return new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {

			return null;
		}
	}

	public Diagram getDiagram(IFile file) {
		try {
			return DiagramFileUtil.getDiagram(diagramFile);
		} catch (IOException e) {
			GUIEditorPlugin.logError(e.getMessage(), e);
		} catch (CoreException e) {
			GUIEditorPlugin.logError(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			GUIEditorPlugin.logError(e.getMessage(), e);
		}

		return null;
	}

}
