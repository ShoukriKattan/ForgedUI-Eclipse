/**
 * 
 */
package com.forgedui.editor.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * @author shoukry
 * 
 */
public class SecurityUtils {

	public static String getMachineMacAddress() {
		InetAddress ip;
		try {

			ip = InetAddress.getLocalHost();

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i],
						(i < mac.length - 1) ? "-" : ""));
			}

			return sb.toString();

		} catch (Exception e) {

		}
		return "UNKNOWN_MAC_ADDRESS";
	}

	public static String hashSlient(String toHash) {
		try {
			return hash(toHash);
		} catch (Exception e) {

			return "";
		}
	}

	public static String hash(String stringToHash)
			throws NoSuchAlgorithmException {

		try {

			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] dst = md.digest(stringToHash.getBytes());

			return Base64.encodeBase64URLSafeString(dst);

		} catch (Exception e) {
			return "";
		}

	}

	public static void main(String[] args) {
		System.out.println(getMachineMacAddress());
	}
}
