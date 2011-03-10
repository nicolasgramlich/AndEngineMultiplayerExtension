package org.anddev.andengine.extension.multiplayer.protocol.util;

import java.util.regex.Pattern;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * @author Nicolas Gramlich
 * @since 17:16:20 - 20.06.2010
 */
public class IPUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final String LOCALHOST_IP = "127.0.0.1";

	private static final String REGEXP_255 = "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)"; // 25(<=5) or 2(<=4)(<=9) or (0|1)(<=9)(<=9)
	public static final String REGEXP_IP = REGEXP_255 + "\\." + REGEXP_255 + "\\." + REGEXP_255 + "\\." + REGEXP_255;

	private static final Pattern IP_PATTERN = Pattern.compile(REGEXP_IP);

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static String getIPAddress(final Context pContext) {
		final WifiManager wifiManager = (WifiManager) pContext.getSystemService(Context.WIFI_SERVICE);
		return IPUtils.ipAddressToString(wifiManager.getConnectionInfo().getIpAddress());
	}

	public static String ipAddressToString(int pIPAddress) {
		final StringBuilder sb = new StringBuilder();
		sb.append(pIPAddress  & 0xff).append('.')
		.append((pIPAddress >>>= 8) & 0xff).append('.')
		.append((pIPAddress >>>= 8) & 0xff).append('.')
		.append((pIPAddress >>>= 8) & 0xff);
		return sb.toString();
	}

	public static boolean isValidIP(final String pIPAddress) {
		return IP_PATTERN.matcher(pIPAddress).matches();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
