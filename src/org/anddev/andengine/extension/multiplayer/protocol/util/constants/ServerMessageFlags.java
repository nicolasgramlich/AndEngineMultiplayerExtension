package org.anddev.andengine.extension.multiplayer.protocol.util.constants;

/**
 * @author Nicolas Gramlich
 * @since 18:23:13 - 19.09.2009
 */
public interface ServerMessageFlags {
	// ===========================================================
	// Final Fields
	// ===========================================================

	public static final short COUNT_FLAG_MESSAGE_SERVER_CONNECTION = 64;

	public static final short INDEX_FLAG_MESSAGE_SERVER_CONNECTION_FIRST = Short.MIN_VALUE;
	public static final short INDEX_FLAG_MESSAGE_SERVER_CONNECTION_LAST = INDEX_FLAG_MESSAGE_SERVER_CONNECTION_FIRST + COUNT_FLAG_MESSAGE_SERVER_CONNECTION - 1;

	/* Connection ServerMessage */
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED = INDEX_FLAG_MESSAGE_SERVER_CONNECTION_FIRST;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_REFUSED = FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED + 1;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_CLOSE = FLAG_MESSAGE_SERVER_CONNECTION_REFUSED + 1;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_PING = FLAG_MESSAGE_SERVER_CONNECTION_CLOSE + 1;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_PONG = FLAG_MESSAGE_SERVER_CONNECTION_PING + 1;

	// ===========================================================
	// Methods
	// ===========================================================
}
