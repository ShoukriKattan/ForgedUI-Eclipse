/**
 * 
 */
package com.forgedui.editor.analytics;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.common.ActivationService;
import com.forgedui.editor.util.SecurityUtils;

/**
 * 
 * TODO : reporting services should become a new Plugin of its own byt we dont
 * haev time
 * 
 * @author shoukry
 * 
 *         TODO : Consider using a CSV library like super csv or something TODO:
 *         Consider a more efficient implementation of writing events to the
 *         file ... or bulk writing ... or a queue and write every x seconds or
 *         something
 * 
 */
public class ReportingServiceImpl implements ReportingService {

	private static final String REPORT_FILE_NAME_PREFIX = "report_";

	private static final String REPORT_FILE_NAME_SUFFIX = ".csv";

	File reportingDir = null;

	SimpleDateFormat fileNameDateExtensionFormat;

	SimpleDateFormat eventDateTimeFormat;

	String reportingDirName = "reporting";

	public ReportingServiceImpl() {

		File file = GUIEditorPlugin.getDefault().getStateLocation().toFile();

		// This is the basic dir which we can store files and folders under

		this.reportingDir = new File(file, this.reportingDirName);

		if (!this.reportingDir.exists()) {
			this.reportingDir.mkdir();
		}

		this.fileNameDateExtensionFormat = new SimpleDateFormat("yyyy_MM_dd");
		this.fileNameDateExtensionFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		this.eventDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
		this.eventDateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

	}

	public synchronized void uploadReports() {

		final ILog log = GUIEditorPlugin.getDefault().getLog();

		log.log(new Status(IStatus.INFO, GUIEditorPlugin.PLUGIN_ID, 0,
				"Uploading report process started", (Throwable) null));

		// The files to upload are yesterday's files and more :

		final GregorianCalendar today = new GregorianCalendar();

		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		String[] reportFilesToUpload = this.reportingDir.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {

				int indexOfReport_ = name.indexOf(REPORT_FILE_NAME_PREFIX);
				int lastIndexOfCsv = name.lastIndexOf(REPORT_FILE_NAME_SUFFIX);

				if (indexOfReport_ != -1 && lastIndexOfCsv != -1) {

					String dateS = name.substring(indexOfReport_
							+ REPORT_FILE_NAME_PREFIX.length(), lastIndexOfCsv);
					try {
						Date d = ReportingServiceImpl.this.fileNameDateExtensionFormat.parse(dateS);

						GregorianCalendar fileDate = new GregorianCalendar();
						fileDate.setTime(d);

						fileDate.set(Calendar.HOUR, 0);
						fileDate.set(Calendar.MINUTE, 0);
						fileDate.set(Calendar.SECOND, 0);
						fileDate.set(Calendar.MILLISECOND, 0);

						if (fileDate.before(today) && !fileDate.equals(today)) {

							log.log(new Status(IStatus.INFO,
									GUIEditorPlugin.PLUGIN_ID, 0,
									"Found file to be uploaded " + name,
									(Throwable) null));

							return true;
						}

					} catch (ParseException e) {

					}

				}

				return false;
			}
		});

		String userId = ActivationService.getInstance().getCurrentUser("anonymous");

		String uploadAPI = ActivationService.getBaseURL() + "ForgedUI/reporting/receive";

		for (String reportFile : reportFilesToUpload) {

			log.log(new Status(IStatus.INFO, GUIEditorPlugin.PLUGIN_ID, 0,
					"Uploading report file " + reportFile, (Throwable) null));

			String theToken = SecurityUtils.hashSlient("[" + userId
					+ "][reportingSecretTokenABCD]");

			String finalURL = uploadAPI + "?user=" + userId + "&fname="
					+ reportFile + "&token=" + theToken;

			PutMethod pm = new PutMethod(finalURL);

			pm.setRequestEntity(new FileRequestEntity(new File(this.reportingDir,
					reportFile), "text/csv"));

			HttpClient client = ActivationService.newHttpClient();

			try {

				client.executeMethod(pm);

				if (pm.getStatusCode() == HttpStatus.SC_CREATED) {

					log.log(new Status(IStatus.INFO, GUIEditorPlugin.PLUGIN_ID,
							0, "File uploaded successfully " + reportFile,
							(Throwable) null));

					File f = new File(this.reportingDir, reportFile);
					f.delete();

				}

			} catch (Exception e) {

				log.log(new Status(IStatus.ERROR, GUIEditorPlugin.PLUGIN_ID, 0,
						"Failed to upload report file " + reportFile,
						e));

			} finally {
				pm.releaseConnection();
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.forgedui.editor.analytics.ReportingService#reportEvent(com.forgedui
	 * .editor.analytics.ReportingEventTypes, java.lang.String[])
	 */
	@Override
	public void reportEvent(ReportingEventTypes eventType, String[] data) {
		// How about a simple CSV file which says
		// Event A, Time
		// Event B, Time
		// Event C, Time

		Date date = new Date();

		String fileName = REPORT_FILE_NAME_PREFIX
				+ this.fileNameDateExtensionFormat.format(date)
				+ REPORT_FILE_NAME_SUFFIX;

		// Collect and upload them once per ... Day .. Week ...

		// Amature CSV writing .. consider using Super CSV or some pre-existing
		// lib instead

		StringBuilder sb = new StringBuilder();

		escapeCSV(sb, this.eventDateTimeFormat.format(date));

		sb.append(',');

		escapeCSV(sb, eventType.toString());

		sb.append(',');

		for (int i = 0; i < data.length; i++) {

			escapeCSV(sb, data[i]);

			if (i != data.length - 1) {
				sb.append(',');
			}
		}

		sb.append(IOUtils.LINE_SEPARATOR);

		FileWriter fw = null;

		try {

			// Not too happy but i swear i dont have time ...
			fw = new FileWriter(new File(this.reportingDir, fileName), true);
			fw.write(sb.toString());
			fw.flush();
		} catch (IOException e) {

		} finally {
			IOUtils.closeQuietly(fw);
		}

	}

	public static StringBuilder escapeCSV(StringBuilder builder, String value) {

		builder.append('"');
		builder.append(value);
		builder.append('"');

		return builder;

	}

	@Override
	public void reportError() {
		// TODO Auto-generated method stub

	}

}
