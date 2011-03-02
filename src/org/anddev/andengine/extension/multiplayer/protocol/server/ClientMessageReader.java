package org.anddev.andengine.extension.multiplayer.protocol.server;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionCloseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionEstablishClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPingClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ClientMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 15:26:29 - 18.09.2009
 */
public class ClientMessageReader extends BaseMessageReader<BaseClientMessage> implements ClientMessageFlags, IClientMessageReader {
	// ===========================================================
	// Constants
	// ===========================================================

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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class DefaultClientMessageReader extends ClientMessageReader {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public DefaultClientMessageReader() {
			this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH, ConnectionEstablishClientMessage.class);
			this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE, ConnectionCloseClientMessage.class);
			this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PING, ConnectionPingClientMessage.class);
			this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PONG, ConnectionPongClientMessage.class);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
