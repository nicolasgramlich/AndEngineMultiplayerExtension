package org.anddev.andengine.extension.multiplayer.protocol.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.anddev.andengine.util.DataUtils;

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

	public static String ipAddressToString(int pIPAddress) {
		final StringBuilder sb = new StringBuilder();
		sb.append(pIPAddress & 0xff).append('.')
		.append((pIPAddress >>>= 8) & 0xff).append('.')
		.append((pIPAddress >>>= 8) & 0xff).append('.')
		.append((pIPAddress >>>= 8) & 0xff);
		return sb.toString();
	}

	public static String ipAddressToString(final byte[] pIPAddress) {
		final StringBuilder sb = new StringBuilder();
		final int length = pIPAddress.length;
		for(int i = 0; i < length; i++) {
			sb.append(DataUtils.unsignedByteToInt(pIPAddress[i]));
			if(i < length -1) {
				sb.append('.');
			}
		}
		return sb.toString();
	}

	public static int stringToIPAddress(final String pString) {
		final Matcher matcher = IP_PATTERN.matcher(pString);
		if(matcher.matches() && matcher.groupCount() == 4) {
			final int byte0 = Integer.parseInt(matcher.group(1));
			final int byte1 = Integer.parseInt(matcher.group(2));
			final int byte2 = Integer.parseInt(matcher.group(3));
			final int byte3 = Integer.parseInt(matcher.group(4));

			return byte0 << 0 | byte1 << 8 | byte2 << 16 | byte3 << 24;
		}
		throw new IllegalArgumentException("Could not parse IP: '" + pString + "' !");
	}

	public static boolean isValidIP(final String pIPAddress) {
		return IP_PATTERN.matcher(pIPAddress).matches();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
