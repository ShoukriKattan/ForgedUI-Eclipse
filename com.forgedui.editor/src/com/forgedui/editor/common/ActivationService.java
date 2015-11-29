/**
 * 
 */
package com.forgedui.editor.common;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.prefs.BackingStoreException;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.exception.InvalidActivationCodeException;
import com.forgedui.editor.exception.InvalidActivationException;
import com.forgedui.editor.exception.LicenseTamperedException;
import com.forgedui.editor.exception.NotActivatedException;
import com.forgedui.editor.exception.ServerCommunicationException;
import com.forgedui.editor.exception.ServerErrorException;
import com.forgedui.editor.exception.ServerResponseTamperedException;
import com.forgedui.editor.util.SecurityUtils;

/**
 * @author shoukry
 * 
 */
public class ActivationService {

	public static final Long UNLIMITED_CODE_GENS = -1000L;

	public static final String USER_ID_PROP_NAME = "com.forgedui.user.id";

	public static final String ACTIVATION_V_PROP_NAME = "com.forgedui.activation.version";

	private static String HTTP_PROTOCOL = "http://";
	private static String HOST = "app.forgedui.com";
	private static int PORT=80;

	private static ActivationService INSTANCE;

	private ILog log;

	private ActivationService(ILog logging) {

		this.log = logging;

	}

	/**
	 * @param string
	 * @param e
	 */
	public void logError(String s, Exception e) {

		this.log.log(new Status(IStatus.ERROR, GUIEditorPlugin.PLUGIN_ID, s, e));

	}

	public void info(String info) {
		this.log.log(new Status(IStatus.INFO, GUIEditorPlugin.PLUGIN_ID, info));
	}

	public static HttpClient newHttpClient() {

		HttpClient client = new HttpClient();

		// Socket timeout 30 seconds
		client.getParams().setParameter("http.socket.timeout",
 new Integer(30000));
		client.getParams().setParameter("http.connection.timeout",
 new Integer(30000));

		return client;
	}

	public static String getBaseURL() {
		return HTTP_PROTOCOL + HOST +":"+PORT+ "/";
	}

	public static void start(ILog log) {

		INSTANCE = new ActivationService(log);

	}

	public static ActivationService getInstance() {

		return INSTANCE;

	}

	public void clearActivation() {

		IEclipsePreferences prefs = new ConfigurationScope()
				.getNode(GUIEditorPlugin.PLUGIN_ID);

		prefs.remove(USER_ID_PROP_NAME);
		prefs.remove(ACTIVATION_V_PROP_NAME);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			logError("Error resetting pres", e);
		}

	}

	public String getCurrentUser() {
		return getCurrentUser(null);
	}

	public String getCurrentUser(String defaultU) {

		return getCodeGenPreferences().get(USER_ID_PROP_NAME, defaultU);

	}

	public void activatePluginLicense(String licenseCode)
			throws ServerCommunicationException, LicenseTamperedException,
			InvalidActivationException, InvalidActivationCodeException,
			Exception {

		licenseCode = licenseCode.trim();

		String secretKey = "fuiSharedShoukry54321";

		int indexOfUnderscore = licenseCode.indexOf('_');

		if (indexOfUnderscore != -1) {

			String id = licenseCode.substring(0, indexOfUnderscore);
			String receivedCode = licenseCode.substring(indexOfUnderscore + 1);

			String toBeSigned = id + "_" + secretKey;

			String signed = SecurityUtils.hashSlient(toBeSigned);

			if (receivedCode.equals(signed)) {

				// Put the user ID and we are ready

				handleActivation(id);
				return;

			}

		}

		throw new InvalidActivationCodeException();
	}

	public IEclipsePreferences getCodeGenPreferences() {

		// This is not really Code gen preferences , but its a sneaky method
		// name for anyone who tries to reverse engineer
		IEclipsePreferences prefs = new ConfigurationScope()
				.getNode(GUIEditorPlugin.PLUGIN_ID);

		return prefs;

	}

	private void handleActivation(String user)
			throws ServerCommunicationException, LicenseTamperedException,
			InvalidActivationException, InvalidActivationCodeException,
			Exception {

		try {

			IEclipsePreferences prefs = getCodeGenPreferences();

			if ((user != null && user.length() > 0)) {
				// Only if we have a user we go to server

				// Go to server if : There is no local license , if local
				// license is demo and no attempts remaining, if local license
				// is demo and certain expiry , or if local license is
				// full and certain threshold.

				try {

					JSONObject result = sendActivationRequest(user);

					if ("success".equals(result.getString("status"))) {

						// Preferences putting user id
						prefs.put(USER_ID_PROP_NAME, user);
						prefs.put(ACTIVATION_V_PROP_NAME, "1.1");

						prefs.flush();

						return;
					} else {

						// Error status : how to handle

						clearActivation();

						String error = result.getString("msg");

						throw new InvalidActivationException(error);

					}

				} catch (ServerCommunicationException e) {
					throw e;
				} catch (RuntimeException e) {

					throw e;

				} catch (Exception e) {

					throw e;
				}

			} else {
				// user is null
				throw new InvalidActivationCodeException();
			}
		} finally {

		}

	}

	public JSONObject sendActivationRequest(String userId) throws Exception {

		// POST SYSTEM parameters to make sure

		// This method has been renamed parse and get JOSN on purpose .. For
		// security
		// this method contacts registration server

		String fuiKey = "fuiSharedShoukry54321";

		// String servKey = "fuiServerShoukryKing78127";

		String osUserName = System.getProperty("user.name");
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String osVersion = System.getProperty("os.version");
		String macAddress = SecurityUtils.getMachineMacAddress();

		String api = ActivationService.getBaseURL()
 + "ForgedUI/licensing/activate";

		String theHash = SecurityUtils.hash("[" + userId + "][" + osUserName
				+ "][" + osName + "][" + osArch + "][" + osVersion + "]["
				+ fuiKey + "]");

		PostMethod pm = new PostMethod(api);

		NameValuePair[] nvps = new NameValuePair[7];
		nvps[0] = new NameValuePair("username", userId);
		nvps[1] = new NameValuePair("osuser", osUserName);
		nvps[2] = new NameValuePair("osname", osName);
		nvps[3] = new NameValuePair("osarch", osArch);
		nvps[4] = new NameValuePair("osversion", osVersion);
		nvps[5] = new NameValuePair("mcadd", macAddress);
		nvps[6] = new NameValuePair("hash", theHash);

		pm.addParameters(nvps);

		try {

			HttpClient client = ActivationService.newHttpClient();

			client.executeMethod(pm);

			if (pm.getStatusCode() != HttpStatus.SC_OK) {

				throw new ServerCommunicationException();
			}

			// if all is ok

			JSONObject jsonObject = new JSONObject(pm.getResponseBodyAsString());

			return jsonObject;

		} catch (HttpException e) {

			throw new ServerCommunicationException();

		} catch (IOException e) {
			throw new ServerCommunicationException();

		} catch (JSONException e) {

			throw new ServerCommunicationException();

		} catch (Exception e) {

			GUIEditorPlugin.logError(
					"Error while attempting to check for license", e);
			throw e;

		} finally {

			pm.releaseConnection();

		}

	}

	public Long connectAndInc() throws NoSuchAlgorithmException,
			ServerCommunicationException, ServerErrorException,
			ServerResponseTamperedException, NotActivatedException {

		String user = getCurrentUser();

		if (user == null) {
			throw new NotActivatedException();
		}

		String fuiKey = "fuiSharedShoukry54321";

		String servKey = "fuiServerShoukryKing78127";

		HttpClient client = ActivationService.newHttpClient();

		String osUserName = System.getProperty("user.name");
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String osVersion = System.getProperty("os.version");

		String mac = SecurityUtils.getMachineMacAddress();

		long currentTime = new Date().getTime();
		String currentTimeString = "" + currentTime;
		String theHash = SecurityUtils.hash("[" + user + "][" + osUserName
				+ "][" + osName + "][" + osArch + "][" + osVersion + "]["
 + fuiKey + "][" + currentTimeString + "]");

		NameValuePair[] nvps = new NameValuePair[8];
		nvps[0] = new NameValuePair("account", user);
		nvps[1] = new NameValuePair("osuser", osUserName);
		nvps[2] = new NameValuePair("osname", osName);
		nvps[3] = new NameValuePair("osarch", osArch);
		nvps[4] = new NameValuePair("osversion", osVersion);
		nvps[5] = new NameValuePair("mcadd", mac);
		nvps[6] = new NameValuePair("sec", theHash);
		nvps[7] = new NameValuePair("time", currentTimeString);

		// How many attempts remaining . 0 means none and dont generate

		PostMethod postMethod = new PostMethod(ActivationService.getBaseURL()+ "ForgedUI/licensing/codegen");

		postMethod.addParameters(nvps);

		try {
			client.executeMethod(postMethod);

			if (postMethod.getStatusCode() != HttpStatus.SC_OK) {

				throw new ServerCommunicationException();
			}

			JSONObject jsonObject = new JSONObject(
					postMethod.getResponseBodyAsString());

			String status = jsonObject.getString("status");
			String signature = jsonObject.getString("signature");
			
			
			if (!"success".equals(status)) {
				throw new ServerErrorException();
			}

			String localSig = SecurityUtils.hash("[" + status + "][" + servKey + "]["+ currentTimeString + "]");

			if (!localSig.equals(signature)) {
				throw new ServerResponseTamperedException();
			}
			return UNLIMITED_CODE_GENS;

		} catch (HttpException e) {
			throw new ServerCommunicationException();
		} catch (IOException e) {
			throw new ServerCommunicationException();
		} catch (JSONException e) {
			throw new ServerCommunicationException();
		} finally {
			postMethod.releaseConnection();
		}

	}

}
